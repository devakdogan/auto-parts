package com.ape.business.abstracts;

import com.ape.entity.concrete.CreditCardEntity;
import com.ape.entity.dto.CreditCardDTO;

import java.util.List;

public interface CreditCardService {
    CreditCardEntity getCreditCardById(Long creditCardId);
    List<CreditCardDTO> getAllPaymentInfoForAuthUser();
    CreditCardDTO getAuthUserCreditCardInfoWithID(Long creditCardId);
}
