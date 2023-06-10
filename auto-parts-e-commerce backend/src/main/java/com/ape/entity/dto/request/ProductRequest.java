package com.ape.entity.dto.request;


import com.ape.entity.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "You must provide a title")
    @Size(min = 5, max = 150, message = "The Title you have entered '${validatedValue}' must be between {min} and {max} character long")
    private String title;
    @NotBlank
    @Size(max = 500, message = "The short description you have entered '${validatedValue}' must be {max} character long")
    private String shortDesc;
    @NotBlank
    @Size(max = 3500, message = "The long description you have entered '${validatedValue}' must be {max} character long" )
    private String longDesc;
    private Double price;
    private Double tax;
    @Min(0)
    @Max(100)
    private Integer discount;
    private Integer stockAmount;
    private Integer stockAlarmLimit;
    private Set<String> imageId;
    private ProductStatus status;
    private Double width;
    private Double length;
    private Double height;
    private Long categoryId;
    private Long brandId;
}
