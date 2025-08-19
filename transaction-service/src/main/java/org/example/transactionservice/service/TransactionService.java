package org.example.transactionservice.service;

import lombok.RequiredArgsConstructor;
import org.example.transactionservice.dto.ExternalTransferDto;
import org.example.transactionservice.dto.GetAccountDto;
import org.example.transactionservice.dto.TransactionDto;
import org.example.transactionservice.exception.BusinessException;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.repository.ITransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final ITransactionRepository repository;
    private final AccountService accountService;
    private final TransferPublisher transferPublisher;

    public Flux<Transaction> getAll() {
        return repository.findAll();
    }

    public Flux<Transaction> getByAccountId(Long accountId) {
        return repository.findByAccountId(accountId);
    }

    public Mono<String> saveExternalTransaction(ExternalTransferDto externalTransferDto) {
        var accountMovement = externalTransferDto.getAccount();
        var transactionMovement = externalTransferDto.getTransaction();
        return accountService.updateBalance(accountMovement.getId(), accountMovement.toBuilder()
                        .balance(accountMovement.getBalance().subtract(transactionMovement.getAmount().multiply(transactionMovement.getTax())))
                        .build())
                .flatMap(accountMovementSaved -> {
                    transactionMovement.setId(null);
                    return repository.save(transactionMovement)
                            .thenReturn("External Transaction Successful, Nro of Transaction: " + transactionMovement.getTransactionKey());
                });
    }

    public Mono<String> createTransaction(TransactionDto transactionDto) {
        if (transactionDto.getDestinationAccountId() == null || transactionDto.getSourceAccountId() == null
                || transactionDto.getAmount() == null) {
            return Mono.error(new BusinessException("ALl fields are required: sourceAccountId, destinationAccountId, and amount"));
        }

        return accountService.getInfoAccountByAccountId(transactionDto.getSourceAccountId())
                .zipWith(accountService.getInfoAccountByAccountId(transactionDto.getDestinationAccountId()))
                .flatMap(accounts -> this.validateTransaction(transactionDto, accounts))
                .flatMap(accounts -> {
                    if (!accounts.getT1().getBankId().equals(accounts.getT2().getBankId())) {
                        return transferToExternalBank(transactionDto, accounts);
                    }
                    return transferToSameBank(transactionDto, accounts);
                }).onErrorResume(throwable -> Mono.just("Transaction Failed, please check the details: " + throwable.getMessage()));
    }

    private Mono<String> transferToSameBank(TransactionDto transactionDto, Tuple2<GetAccountDto, GetAccountDto> accounts) {
        return movementAccountValues(transactionDto, accounts)
                .flatMap(accountsMovement -> {
                    var transactionAccountSource = new Transaction();
                    transactionAccountSource.setId(null);
                    transactionAccountSource.setAccountId(transactionDto.getSourceAccountId());
                    transactionAccountSource.setTransactionType("RETIRO");
                    transactionAccountSource.setCreatedAt(LocalDateTime.now());
                    transactionAccountSource.setAmount(transactionDto.getAmount());
                    transactionAccountSource.setTax(BigDecimal.ZERO);
                    transactionAccountSource.setTransactionKey(UUID.randomUUID().toString());
                    return repository.save(transactionAccountSource)
                            .flatMap(transactionAccountDestination -> {
                                transactionAccountDestination.setId(null);
                                transactionAccountDestination.setAccountId(transactionDto.getDestinationAccountId());
                                transactionAccountDestination.setTransactionType("DEPOSITO");
                                return repository.save(transactionAccountDestination)
                                        .thenReturn("Transaction Successful, Nro of Transaction: " + transactionAccountDestination.getTransactionKey());
                            });
                });
    }

    private Mono<String> transferToExternalBank(TransactionDto transactionDto,
                                                Tuple2<GetAccountDto, GetAccountDto> accounts) {
        return accountService.updateBalance(accounts.getT1().getId(), accounts.getT1().toBuilder()
                        .balance(accounts.getT1().getBalance().subtract(transactionDto.getAmount())).build())
                .flatMap(accountsMovement -> {
                    var transactionAccountSource = new Transaction();
                    transactionAccountSource.setId(null);
                    transactionAccountSource.setAccountId(transactionDto.getSourceAccountId());
                    transactionAccountSource.setTransactionType("RETIRO");
                    transactionAccountSource.setCreatedAt(LocalDateTime.now());
                    transactionAccountSource.setAmount(transactionDto.getAmount());
                    transactionAccountSource.setTax(BigDecimal.ZERO);
                    transactionAccountSource.setTransactionKey(UUID.randomUUID().toString());
                    return repository.save(transactionAccountSource)
                            .flatMap(transactionAccountDestination -> {
                                transactionAccountDestination.setId(null);
                                transactionAccountDestination.setAccountId(transactionDto.getDestinationAccountId());
                                transactionAccountDestination.setTransactionType("DEPOSITO");
                                return transferPublisher.publishTransferEvent(ExternalTransferDto.builder()
                                                .account(accounts.getT2().toBuilder()
                                                        .balance(accounts.getT2().getBalance().add(transactionDto.getAmount()))
                                                        .build())
                                                .transaction(transactionAccountDestination).build())
                                        .thenReturn("Transaction Successful, Nro of Transaction: " + transactionAccountDestination.getTransactionKey());
                            });
                });
    }

    private Mono<Tuple2<GetAccountDto, GetAccountDto>> movementAccountValues(TransactionDto transactionDto, Tuple2<GetAccountDto, GetAccountDto> accounts) {
        return Mono.zip(accountService.updateBalance(accounts.getT1().getId(), accounts.getT1().toBuilder()
                        .balance(accounts.getT1().getBalance().subtract(transactionDto.getAmount())).build()),
                accountService.updateBalance(accounts.getT2().getId(), accounts.getT2().toBuilder()
                        .balance(accounts.getT2().getBalance().add(transactionDto.getAmount())).build()));
    }


    private Mono<Tuple2<GetAccountDto, GetAccountDto>> validateTransaction(TransactionDto transaction,
                                                                           Tuple2<GetAccountDto, GetAccountDto> accounts) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            return Mono.error(new BusinessException("Transaction amount must be greater than zero"));
        }
        if (transaction.getAmount().compareTo(accounts.getT1().getBalance()) > 0) {
            return Mono.error(new BusinessException("Insufficient balance in source account"));
        }
        if (transaction.getSourceAccountId().equals(transaction.getDestinationAccountId())) {
            return Mono.error(new BusinessException("Source and destination accounts cannot be the same"));
        }
        return Mono.just(accounts);
    }
}
