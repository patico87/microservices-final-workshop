package org.example.transferservice.listener;

import lombok.RequiredArgsConstructor;
import org.example.transferservice.dto.ExternalTransferDto;
import org.example.transferservice.service.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransferSubscriber {
    private final TransactionService transactionService;

    @RabbitListener(queues = "transfer-queue")
    public void receiveExternalTransfer(ExternalTransferDto externalTransferDto) {

        var transaction = externalTransferDto.getTransaction();
        transactionService.createExternalTransfer(externalTransferDto.toBuilder()
                        .transaction(transaction.toBuilder()
                                .tax(BigDecimal.valueOf(0.15)).build())
                        .build())
                .subscribe();
    }
}
