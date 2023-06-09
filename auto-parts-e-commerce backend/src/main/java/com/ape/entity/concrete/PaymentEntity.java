package com.ape.entity.concrete;

import com.ape.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "t_payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double amount;

    @Column
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentStatus status;

    @Column(nullable = false)
    private LocalDateTime createAt=LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;
}
