package org.example.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionDto {
    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "";
    }
}
