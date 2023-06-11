package com.ape.entity.dto;


import com.ape.entity.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    private String sku;

    private String title;

    private String shortDesc;

    private String longDesc;

    private Double price;

    private Double discountedPrice;

    private Double tax;

    private Integer discount;

    private Integer stockAmount;

    private Integer stockAlarmLimit;

    private String slug;

    private Set<ShowcaseImageDTO> images;

    private ProductStatus status;

    private Double width;

    private Double length;

    private Double height;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private ProductBrandDTO brand;

    private ProductCategoryDTO category;
}
