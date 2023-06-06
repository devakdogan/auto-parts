package com.ape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ShoppingCartItemDTO {

    private Long id;
    private Long productId;

    private Long quantity;

    private String title;

    private String imageId;

    private Double unitPrice;

    private Double discountedPrice;
    private Integer stockAmount;

    private Double totalPrice;

    private Integer discount;
    private Double tax;

}
