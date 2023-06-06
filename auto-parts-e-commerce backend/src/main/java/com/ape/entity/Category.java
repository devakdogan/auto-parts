package com.ape.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ape.entity.enums.CategoryStatus;
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
@Table(name="t_category")
public class Category {

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
    private List<Product> product = new ArrayList<>();



}

