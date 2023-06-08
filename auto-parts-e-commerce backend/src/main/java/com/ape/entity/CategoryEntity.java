package com.ape.entity;

import com.ape.entity.enums.CategoryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="t_category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 80, nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column
    private CategoryStatus status;
    @Column(nullable = false)
    private  Boolean builtIn = false;
    @Column
    private LocalDateTime createAt=LocalDateTime.now();
    @Column
    private LocalDateTime updateAt;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<ProductEntity> product = new ArrayList<>();



}

