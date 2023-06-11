package com.ape.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long productId;

    private String imageId;

    private String title;

    private Integer quantity;

    private Double unitPrice;

    private double tax;

    private double discount;

    private Double subTotal;

}
