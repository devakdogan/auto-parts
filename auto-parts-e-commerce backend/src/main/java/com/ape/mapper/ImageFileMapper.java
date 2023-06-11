package com.ape.mapper;

import com.ape.entity.concrete.ImageFileEntity;
import com.ape.entity.dto.ShowcaseImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ImageFileMapper {

    @Mapping(source = "id",target = "imageId")
    ShowcaseImageDTO entityToShowcaseDTO(ImageFileEntity imageFile);
}
