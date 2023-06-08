package com.ape.dao;

import com.ape.entity.ImageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDao extends JpaRepository<ImageFileEntity,String> {
}
