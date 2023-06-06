package com.ape.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String cartUUID;
    @Column
    private Double grandTotal = 0.0;
    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @OneToMany(mappedBy = "shoppingCart",orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItem = new ArrayList<>();

}
