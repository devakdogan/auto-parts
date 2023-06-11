package com.ape.business.concretes;

import com.ape.business.abstracts.ContactMessageService;
import com.ape.business.abstracts.EmailService;
import com.ape.business.abstracts.UserService;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.dto.request.ContactMessageRequest;
import com.ape.entity.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactMessageManager implements ContactMessageService {

    private final EmailService emailService;
    private final EmailManager emailManager;
    private final UserService userService;

    @Override
    public void sendMessage(ContactMessageRequest contactMessageRequest) {
        List<UserEntity> adminList = userService.findUserByRole(RoleType.ROLE_ADMIN);
        for (UserEntity each:adminList) {
            emailService.send(
                    each.getEmail(),
                    emailManager.buildContactMessage(each.getFirstName(),contactMessageRequest)
            );
        }

    }

}
