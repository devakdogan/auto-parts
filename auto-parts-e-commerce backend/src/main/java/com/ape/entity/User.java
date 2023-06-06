package com.ape.entity;

import com.ape.entity.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class User {
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

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus status;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Max(5)
    private byte loginFailCount = 0;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @ManyToMany
    @JoinTable(name = "t_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private Set<ConfirmationToken> confirmationTokens = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

}
