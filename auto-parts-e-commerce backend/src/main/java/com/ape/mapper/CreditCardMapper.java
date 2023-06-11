package com.ape.mapper;

import com.ape.entity.concrete.CreditCardEntity;
import com.ape.entity.dto.CreditCardDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    CreditCardDTO entityToDTO(CreditCardEntity creditCard);

    List<CreditCardDTO> entityListToDTOList(List<CreditCardEntity> creditCardList);
}
