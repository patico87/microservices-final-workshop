package org.example.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.repository.NoRepositoryBean;

@Data
@AllArgsConstructor
@NoRepositoryBean
@Builder(toBuilder = true)
public class ExternalTransferDto {
    private TransactionDto transaction;
    private GetAccountDto account;
}
