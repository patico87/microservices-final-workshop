package org.example.accountservice.repository;

import org.example.accountservice.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IAccountRepository  extends ReactiveCrudRepository<Account, Long> {
    Mono<Account> findBynumber(String number);
}
