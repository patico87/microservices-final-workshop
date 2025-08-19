package org.example.bankservice.service;

import org.example.bankservice.exception.BusinessException;
import org.example.bankservice.model.Bank;
import org.example.bankservice.repository.IBanksRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class BanksService {
    private final IBanksRepository repository;

    public BanksService(IBanksRepository repository) {
        this.repository = repository;
    }

    public Flux<Bank> getAll() {
        return repository.findAll();
    }

    public Mono<Bank> getById(Long id) {
        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found")));
    }

    public Mono<Bank> create(Bank bank) {
        bank.setId(null);
        bank.setCreatedAt(LocalDateTime.now());
        return repository.save(bank);
    }

    public Mono<Bank> update(Long id, Bank bank) {
        return repository.findById(id)
                .flatMap(existingBank -> {
                    existingBank.setName(bank.getName());
                    existingBank.setAddress(bank.getAddress());
                    existingBank.setCity(bank.getCity());
                    existingBank.setPhone(bank.getPhone());
                    existingBank.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existingBank);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found for update")));
    }
}
