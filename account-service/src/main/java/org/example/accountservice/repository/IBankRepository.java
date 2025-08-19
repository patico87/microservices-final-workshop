package org.example.accountservice.repository;

import org.example.accountservice.model.Bank;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface IBankRepository extends ReactiveCrudRepository<Bank, Long> {
    Flux<Bank> findAllByAccountId(Long accountId);
}
