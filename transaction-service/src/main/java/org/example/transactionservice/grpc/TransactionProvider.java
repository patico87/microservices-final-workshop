package org.example.transactionservice.grpc;

import io.grpc.stub.StreamObserver;
import org.example.transactionservice.exception.BusinessException;
import org.example.transactionservice.repository.ITransactionRepository;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class TransactionProvider extends TransactionServiceGrpc.TransactionServiceImplBase {
    private final ITransactionRepository repository;

    public TransactionProvider(ITransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getTransactions(TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
        repository.findByAccountId(request.getAccountId())
                .collectList()
                .map(transactions -> {
                    TransactionResponse.Builder responseBuilder = TransactionResponse.newBuilder();
                    transactions.forEach(transaction ->
                            responseBuilder.addTransactions(Transaction.newBuilder()
                                    .setId(transaction.getId())
                                    .setAccountId(transaction.getAccountId())
                                    .setTransactionType(transaction.getTransactionType())
                                    .setAmount(transaction.getAmount().toString())
                                    .setTransactionKey(transaction.getTransactionKey())
                                    .setTax(transaction.getTax().toString())
                                    .build())
                    );
                    return responseBuilder.build();
                })
                .subscribe(responseObserver::onNext,
                        error -> responseObserver.onError(new BusinessException(error.getMessage())),
                        responseObserver::onCompleted)
                .dispose();
    }

}
