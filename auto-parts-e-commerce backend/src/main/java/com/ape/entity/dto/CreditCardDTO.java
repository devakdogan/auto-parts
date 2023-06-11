package com.ape.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDTO {

    private Long id;
    private String title;
    private String nameOnCard;
    private String cardNumber;
    private LocalDate expirationDate;
    private String cvc;

}
