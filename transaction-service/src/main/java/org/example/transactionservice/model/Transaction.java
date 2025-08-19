package org.example.transactionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("transactions")
public class Transaction {

    @Id
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal tax;
    private LocalDateTime createdAt;
    private String transactionKey;
}
