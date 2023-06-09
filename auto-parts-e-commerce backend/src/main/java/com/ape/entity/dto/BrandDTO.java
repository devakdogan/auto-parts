package com.ape.entity.dto;


import com.ape.entity.enums.BrandStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {

    private Long id;

    private String name;

    private BrandStatus status;

    private String image;

    private LocalDateTime createAt = LocalDateTime.now();

    private LocalDateTime updateAt;

}
