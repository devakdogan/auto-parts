package com.ape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="t_user_address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50,nullable=false)
    private String title;
    @Column(length = 30,nullable=false)
    private String firstName;
    @Column(length = 30,nullable=false)
    private String lastName;
    @Column(nullable=false)
    private String phone;
    @Column(length = 80,nullable=false)
    private String email;
    @Column(length = 250,nullable=false)
    private String address;
    @Column(length = 70,nullable=false)
    private String province;
    @Column(length = 70,nullable=false)
    private String city;
    @Column(length = 70,nullable=false)
    private String country;

    @Column
    private LocalDateTime createAt=LocalDateTime.now();
    @Column
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "invoiceAddressEntity")
    private List<OrderEntity> ordersInvoice = new ArrayList<>();

    @OneToMany(mappedBy = "shippingAddressEntity")
    private List<OrderEntity> ordersShipping = new ArrayList<>();
}
