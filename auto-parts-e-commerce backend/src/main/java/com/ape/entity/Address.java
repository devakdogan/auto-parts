package com.ape.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="t_user_address")
public class Address {
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
    private User user;

    @OneToMany(mappedBy = "invoiceAddress")
    private List<Order> ordersInvoice = new ArrayList<>();

    @OneToMany(mappedBy = "shippingAddress")
    private List<Order> ordersShipping = new ArrayList<>();
}
