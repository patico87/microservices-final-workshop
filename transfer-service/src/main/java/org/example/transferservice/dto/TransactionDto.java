package org.example.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionDto {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal tax;
    private LocalDateTime createdAt;
    private String transactionKey;
}
