package org.example.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.accountservice.dto.GetTransactionDto;
import org.example.accountservice.grpc.TransactionConsumer;
import org.example.accountservice.model.Account;
import org.example.accountservice.service.AccountService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionConsumer transactionConsumer;

    @GetMapping
    public Flux<Account> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{accountId}")
    public Mono<Account> getById(@PathVariable Long accountId) {
        return accountService.getById(accountId);
    }

    @PostMapping
    public Mono<Account> create(@RequestBody Account account) {
        return accountService.create(account);
    }

    @PutMapping("/{accountId}")
    public Mono<Account> update(@PathVariable Long accountId, @RequestBody Account account) {
        return accountService.update(accountId, account);
    }

    @GetMapping("/history/{id}")
    public Flux<GetTransactionDto> getHistoryById(@PathVariable Long id) {
        return transactionConsumer.getTransactionHistory(id);
    }
}
