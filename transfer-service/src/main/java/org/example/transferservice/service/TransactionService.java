package org.example.transferservice.service;

import lombok.RequiredArgsConstructor;
import org.example.transferservice.dto.ExternalTransferDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final WebClient.Builder webClientBuilder;
    @Value("${transaction-service.url}")
    private String transactionServiceUrl;


    public Mono<String> createExternalTransfer(ExternalTransferDto externalTransfer) {
        return webClientBuilder.build()
                .post()
                .uri(transactionServiceUrl + "/external")
                .body(Mono.just(externalTransfer), ExternalTransferDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())));
    }

}
