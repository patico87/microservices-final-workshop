package org.example.accountservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.example.accountservice.dto.GetTransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Component
public class TransactionConsumer {
    @Value("${transaction-grpc.host}")
    private String grpcHost;

    @Value("${transaction-grpc.port}")
    private Integer grpcPort;

    private ManagedChannel channel;

    private TransactionServiceGrpc.TransactionServiceBlockingStub stub;

    @PostConstruct
    private void init() {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.stub = TransactionServiceGrpc.newBlockingStub(channel);
    }

    public Flux<GetTransactionDto> getTransactionHistory(Long accountId) {
        TransactionRequest request = TransactionRequest.newBuilder()
                .setAccountId(accountId)
                .build();

        TransactionResponse response = stub.getTransactions(request);
        return Flux.fromIterable(response.getTransactionsList())
                .map(transaction -> GetTransactionDto.builder()
                        .id(transaction.getId())
                        .accountId(transaction.getAccountId())
                        .transactionType(transaction.getTransactionType())
                        .amount(new BigDecimal(transaction.getAmount()))
                        .tax(new BigDecimal(transaction.getTax()))
                        .transactionKey(transaction.getTransactionKey())
                        .build());
    }
}
