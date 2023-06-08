package com.ape.mapper;

import com.ape.dto.BrandDTO;
import com.ape.dto.ProductBrandDTO;
import com.ape.dto.request.BrandRequest;
import com.ape.entity.BrandEntity;
import com.ape.entity.ImageFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(source = "image",target = "image",qualifiedByName = "getImageAsString")
    BrandDTO brandToBrandDTO(BrandEntity brand);
    
    ProductBrandDTO entityToDTO(BrandEntity brand);

    List<BrandDTO> entityListToDTOList(List<BrandEntity> brands);

    BrandEntity brandRequestToBrand(BrandRequest brandRequest);


    @Named("getImageAsString")
    public static String getImage(ImageFileEntity imageFile){
        return imageFile.getId();
    }


}
