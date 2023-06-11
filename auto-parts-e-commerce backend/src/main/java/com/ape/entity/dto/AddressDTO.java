package com.ape.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {


    private Long id;

    private String title;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String address;

    private String province;

    private String city;

    private String country;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Long userId;


}
