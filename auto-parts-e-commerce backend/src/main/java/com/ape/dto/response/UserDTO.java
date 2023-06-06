package com.ape.dto.response;

import com.ape.entity.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private LocalDate birthDate;

    private String email;
    private UserStatus status;
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Set<String> roles;

    private List<FavoriteProductDTO> favoriteList;
}
