package com.ape.entity.dto.request;


import com.ape.entity.enums.BrandStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandUpdateRequest {

    @NotBlank
    @Size(min = 2 , max = 70)
    private String name;

    private BrandStatus status;

    private String image;
}
