package com.ape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_shopping_cart")
public class ShoppingCartEntity {

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
    private List<ShoppingCartItemEntity> shoppingCartItems = new ArrayList<>();
}
