package com.ape.dto.response;

import com.ape.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate birthDate;
    private UserStatus status;
    private String email;
    private byte loginFailCount;
    private Set<String> roles;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
