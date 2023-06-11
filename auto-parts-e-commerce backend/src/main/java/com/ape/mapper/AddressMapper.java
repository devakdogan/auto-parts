package com.ape.mapper;

import com.ape.entity.concrete.AddressEntity;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.dto.AddressDTO;
import com.ape.entity.dto.request.AddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {


    @Mapping(target = "userId",source = "user",qualifiedByName = "getUserIdForAddress")
    AddressDTO entityToDTO(AddressEntity userAddress);

    List<AddressDTO> entityListToDTOList(List<AddressEntity> userAddressList);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createAt",ignore = true)
    @Mapping(target = "updateAt",ignore = true)
    @Mapping(target = "user",ignore = true)
    AddressEntity addressRequestToAddressEntity(AddressRequest addressRequest);

    @Named("getUserIdForAddress")
    public static Long getUserIdForAddress(UserEntity user){
        return user.getId();
    }
}
