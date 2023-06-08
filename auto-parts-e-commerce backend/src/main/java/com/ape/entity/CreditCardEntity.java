package com.ape.entity;

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
    @Column(length = 30,nullable=false)
    private String firstName;
    @Column(length = 30,nullable=false)
    private String lastName;

    @Column(length = 16,nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(length = 3,nullable = false)
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
