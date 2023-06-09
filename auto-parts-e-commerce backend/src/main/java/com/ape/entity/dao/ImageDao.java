package com.ape.entity.dao;

import com.ape.entity.concrete.ImageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDao extends JpaRepository<ImageFileEntity,String> {
}
