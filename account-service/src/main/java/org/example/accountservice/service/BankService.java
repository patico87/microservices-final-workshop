package org.example.accountservice.service;

import org.example.accountservice.dto.GetBankDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BankService {
    private final WebClient.Builder webClientBuilder;

    @Value("${banks-service.url}")
    private String banksServiceUrl;

    public BankService(final WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Boolean> getBank(Long bankId) {
        return webClientBuilder.build()
                .get()
                .uri(banksServiceUrl + "/" + bankId)
                .retrieve()
                .bodyToMono(GetBankDTO.class)
                .map(response -> true)
                .onErrorReturn(false);
    }
}
