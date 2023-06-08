package com.ape.entity;

import com.ape.entity.enums.OrderStatus;
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
@Table(name = "t_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status;

    @Column
    private Double subTotal;

    @Column
    private Double discount;

    @Column
    private Double tax;

    @Column
    private String contactName;

    @Column
    private String contactPhone;

    @Column
    private Double shippingCost;

    @Column
    private String shippingDetails;

    @Column
    private Double grandTotal;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @OneToMany(orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<TransactionEntity> transactions = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<PaymentEntity> payments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "invoice_address_id")
    private AddressEntity invoiceAddressEntity;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private AddressEntity shippingAddressEntity;
}
