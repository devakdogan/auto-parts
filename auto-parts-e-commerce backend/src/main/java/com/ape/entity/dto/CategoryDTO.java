package com.ape.entity.dto;


import com.ape.entity.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    private String title;

    private CategoryStatus status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
