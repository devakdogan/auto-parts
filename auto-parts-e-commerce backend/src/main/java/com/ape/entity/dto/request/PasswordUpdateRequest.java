package com.ape.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {

    @NotBlank(message="Please Provide Old Password")
    private String oldPassword;

    @NotBlank(message="Please Provide New Password")
    private String newPassword;

}
