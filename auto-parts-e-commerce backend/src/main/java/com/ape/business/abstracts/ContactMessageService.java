package com.ape.business.abstracts;

import com.ape.entity.dto.request.ContactMessageRequest;

public interface ContactMessageService {
    void sendMessage(ContactMessageRequest contactMessageRequest);
}
