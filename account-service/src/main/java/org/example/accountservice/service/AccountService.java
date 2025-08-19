package org.example.accountservice.service;

import org.example.accountservice.exception.BusinessException;
import org.example.accountservice.model.Account;
import org.example.accountservice.repository.IAccountRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AccountService {
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    private final IAccountRepository accountRepository;
    private final BankService bankService;

    public AccountService(IAccountRepository accountRepository, BankService bankService) {
        this.accountRepository = accountRepository;
        this.bankService = bankService;
    }

    public Flux<Account> getAll() {
        return accountRepository.findAll();
    }

    public Mono<Account> getById(Long id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ACCOUNT_NOT_FOUND)));
    }

    public Mono<Account> create(Account account) {
        account.setCreatedAt(LocalDateTime.now());
        return bankService.getBank(account.getBankId())
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new BusinessException("Bank not found")))
                .flatMap(unused -> accountRepository.save(account));
    }

    public Mono<Account> update(Long id, Account account) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ACCOUNT_NOT_FOUND)))
                .flatMap(existingAccount -> {
                    existingAccount.setNumber(account.getNumber());
                    existingAccount.setType(account.getType());
                    existingAccount.setBalance(account.getBalance());
                    existingAccount.setUpdatedAt(LocalDateTime.now());
                    return accountRepository.save(existingAccount);
                });
    }
}
