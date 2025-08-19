package org.example.transactionservice.repository;

import org.example.transactionservice.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ITransactionRepository extends ReactiveCrudRepository<Transaction, Long> {

    Flux<Transaction> findByAccountId(Long accountId);
}
