package com.ape.mapper;

import com.ape.entity.concrete.ImageFileEntity;
import com.ape.entity.dto.ShowcaseImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageFileMapper {

    @Mapping(source = "id",target = "imageId")
    ShowcaseImageDTO imageFileToShowcaseImageDTO(ImageFileEntity imageFile);
}
