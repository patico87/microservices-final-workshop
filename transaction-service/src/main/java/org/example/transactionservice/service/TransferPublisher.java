package org.example.transactionservice.service;

import org.example.transactionservice.dto.ExternalTransferDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransferPublisher {
    private final RabbitTemplate rabbitTemplate;

    public TransferPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Void> publishTransferEvent(ExternalTransferDto externalTransfer) {
        rabbitTemplate.convertAndSend("transfer-exchange", "transfer-routing-key", externalTransfer);
        return Mono.empty();
    }
}
