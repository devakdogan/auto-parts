package com.ape.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 30, message = "Your first name ${validatedValue} must be between ${min} and ${max} characters.")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30, message = "Your last name ${validatedValue} must be between ${min} and ${max} characters." )
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //541-317-8828
            message = "Please provide valid phone number")
    private String phone;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Size(min=10, max=80)
    @Email
    private String email;

    private String password;
}
