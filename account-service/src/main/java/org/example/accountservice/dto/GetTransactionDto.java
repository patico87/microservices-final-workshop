package org.example.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GetTransactionDto {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal tax;
    private String transactionKey;
}
