package org.example.accountservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("banks")
public class Bank {
    @Id
    @Column("bank_id")
    private Long id;
    private String name;
    private String address;
    private String city;
    private Float phone;
    @Column("account_id")
    private Long accountId;

    public Bank() {
    }

    public Bank(Long id, String name, String address, String city, Float phone, Long accountId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float getPhone() {
        return phone;
    }

    public void setPhone(Float phone) {
        this.phone = phone;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
