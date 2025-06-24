package com.twd.Pos.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "bakong")
public class Bakong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String hash;
    public String fromAccountId;
    public String toAccountId;
    public String currency;
    public BigDecimal amount;
    public String description;
    public Long createdDateMs;
    public Long acknowledgedDateMs;
    public String trackingStatus;
    public String receiverBank;
    public String receiverBankAccount;
    public String instructionRef;
    public String externalRef;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}