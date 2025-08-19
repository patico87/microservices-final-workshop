package org.example.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetAccountDto {
    private Long id;
    private Long bankId;
    private String number;
    private String type;
    private BigDecimal balance;
}
