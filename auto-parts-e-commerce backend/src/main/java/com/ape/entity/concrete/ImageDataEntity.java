package com.ape.entity.concrete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_image_data")
public class ImageDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    public ImageDataEntity(byte[] data){
        this.data= data;
    }

    public ImageDataEntity(Long id) {
        this.id=id;
    }

}
