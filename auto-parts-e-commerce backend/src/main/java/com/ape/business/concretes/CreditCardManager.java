package com.ape.business.concretes;

import com.ape.business.abstracts.CreditCardService;
import com.ape.business.abstracts.UserService;
import com.ape.entity.concrete.CreditCardEntity;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.dao.CreditCardDao;
import com.ape.entity.dto.CreditCardDTO;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ErrorMessage;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.CreditCardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardManager implements CreditCardService {

    private final UserService userService;
    private final CreditCardDao creditCardDao;
    private final CreditCardMapper creditCardMapper;


    @Override
    public CreditCardEntity getCreditCardById(Long creditCardId) {
        return creditCardDao.findById(creditCardId).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, creditCardId)));
    }

    @Override
    public List<CreditCardDTO> getAllPaymentInfoForAuthUser() {
        UserEntity user = userService.getCurrentUser();
        List<CreditCardEntity> userCartList = creditCardDao.findByUserId(user.getId());
        return creditCardMapper.entityListToDTOList(userCartList);
    }

    @Override
    public CreditCardDTO getAuthUserCreditCardInfoWithID(Long creditCardId) {
        UserEntity user = userService.getCurrentUser();
        CreditCardEntity creditCard = getCreditCardById(creditCardId);
        if (!user.getCreditCards().contains(creditCard)){
            throw new BadRequestException(ErrorMessage.CREDIT_CARD_NOT_FOUND_MESSAGE);
        }
        return creditCardMapper.entityToDTO(creditCard);
    }

    @Override
    public void createPaymentInfo(CreditCardEntity creditCard) {
        List<CreditCardDTO> userCartList = getAllPaymentInfoForAuthUser();
        for (CreditCardDTO each:userCartList) {
            if (creditCard.getTitle().equals(each.getTitle())){
                throw new ConflictException(String.format(ErrorMessage.CREDIT_CARD_TITLE_FOUND_MESSAGE,creditCard.getTitle()));
            }
        }
        creditCardDao.save(creditCard);
    }
}
