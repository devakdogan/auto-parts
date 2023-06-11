package com.ape.mapper;

import com.ape.entity.concrete.CreditCardEntity;
import com.ape.entity.dto.CreditCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    @Mapping(source = "cardNumber",target = "cardNo")
    @Mapping(source = "expirationDate",target = "expireDate")
    CreditCardDTO entityToDTO(CreditCardEntity creditCard);

    List<CreditCardDTO> entityListToDTOList(List<CreditCardEntity> creditCardList);
}
