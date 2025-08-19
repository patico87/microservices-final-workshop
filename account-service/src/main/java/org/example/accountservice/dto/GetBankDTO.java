package org.example.accountservice.dto;

import lombok.Data;

@Data
public class GetBankDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private Float phone;
}
