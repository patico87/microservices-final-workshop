package org.example.bankservice.repository;

import org.example.bankservice.model.Bank;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IBanksRepository extends ReactiveCrudRepository<Bank, Long> {
}
