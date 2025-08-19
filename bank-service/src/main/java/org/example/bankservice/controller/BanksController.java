package org.example.bankservice.controller;

import org.example.bankservice.model.Bank;
import org.example.bankservice.service.BanksService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/banks")
public class BanksController {
    private final BanksService service;

    public BanksController(BanksService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Bank> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Bank> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Mono<Bank> create(@RequestBody Bank bank) {
        return service.create(bank);
    }

    @PutMapping("/{id}")
    public Mono<Bank> update(@PathVariable Long id, @RequestBody Bank bank) {
        return service.update(id, bank);
    }
}

