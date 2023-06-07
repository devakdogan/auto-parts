package com.ape.entity;


import com.ape.entity.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 100, nullable = false)
    private String sku;

    @Column(length = 150, nullable = false)
    private String title;


    @Column(length = 500)
    private String shortDesc;


    @Column(length = 3500)
    private String longDesc;

    @Column
    private Double price;

    @Column
    private Double discountedPrice;

    @Column
    private Double tax;

    @Column
    private Integer discount;

    @Column
    private Integer stockAmount;

    @Column
    private Integer stockAlarmLimit;

    @Column(length = 100, nullable = false)
    private String slug;

    @Column(nullable = false)
    private Boolean featured;

    @Column
    private Boolean newProduct;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductStatus status;
    @Column
    private Double width;

    @Column
    private Double length;

    @Column
    private Double height;

    @Column
    private Boolean builtIn = false;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brandEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "productEntity",cascade = CascadeType.ALL)
    private Set<ShoppingCartItemEntity> shoppingCartItemEntity = new HashSet<>();

    @OneToMany(mappedBy = "productEntity",cascade = CascadeType.ALL)
    private Set<OrderItemEntity> orderItemEntities = new HashSet<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<ImageFileEntity> image;
}
