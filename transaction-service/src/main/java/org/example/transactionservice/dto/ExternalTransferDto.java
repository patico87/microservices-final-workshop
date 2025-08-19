package org.example.transactionservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.transactionservice.model.Transaction;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExternalTransferDto {
    @JsonDeserialize
    Transaction transaction;
    @JsonDeserialize
    GetAccountDto account;
}
