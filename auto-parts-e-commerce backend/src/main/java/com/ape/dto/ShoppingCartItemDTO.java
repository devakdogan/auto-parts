package com.ape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ShoppingCartItemDTO {

    private Long id;
    private Long productId;

    private Long quantity;

    private String title;

    private UUID imageId;

    private Double unitPrice;

    private Double discountedPrice;
    private Integer stockAmount;

    private Double totalPrice;

    private Integer discount;
    private Double tax;

}
