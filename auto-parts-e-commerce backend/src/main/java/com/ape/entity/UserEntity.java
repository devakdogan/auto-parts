package com.ape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30 , nullable = false)
    private String firstName;

    @Column(length = 30 , nullable = false)
    private String lastName;

    @Column(length = 15 , nullable = false)
    private String phone;

    @Column
    private LocalDate birthDate;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Max(5)
    private int loginFailCount = 0;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @Column(name = "locked")
    private Boolean isLocked = false;

    @Column(name = "active")
    private Boolean isActive;

    @ManyToMany
    @JoinTable(name = "t_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private Set<ConfirmationTokenEntity> confirmationToken = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCartEntity shoppingCart;

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private Set<AddressEntity> addresses = new HashSet<>();

}
