package com.ape.dto.request;

import com.ape.entity.enums.CategoryStatus;
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
public class CategoryUpdateRequest {

    @Size(min = 2,max = 80,message = "Your subject '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide the title of the category")
    private String title;

    private CategoryStatus status;
}
