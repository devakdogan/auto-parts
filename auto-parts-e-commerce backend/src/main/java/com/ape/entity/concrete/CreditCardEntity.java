package com.ape.entity.concrete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_credit_card")
public class CreditCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50,nullable=false)
    private String title;
    @Column(length = 50,nullable=false)
    private String nameOnCard;

    @Column(length = 16,nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(length = 3,nullable = false)
    private String cvc;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
