package com.twd.Pos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cashBack = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private String paymentMethod; 

    @Column(nullable = false)
    private boolean isSuccessful;

    @Column(nullable = false, updatable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @OneToMany(mappedBy = "payment")
    private List<Bakong> bakongs;
}
