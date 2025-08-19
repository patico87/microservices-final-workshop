package org.example.transactionservice.controller;

import org.example.transactionservice.dto.ExternalTransferDto;
import org.example.transactionservice.dto.TransactionDto;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public Flux<Transaction> getAll() {
        return transactionService.getAll();
    }

    @PostMapping
    public Mono<String> createTransaction(@RequestBody TransactionDto transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PostMapping("/external")
    public Mono<String> createExternalTransfer(@RequestBody ExternalTransferDto externalTransfer) {
        return transactionService.saveExternalTransaction(externalTransfer);
    }
}
