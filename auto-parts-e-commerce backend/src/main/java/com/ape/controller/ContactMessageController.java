package com.ape.controller;

import com.ape.business.abstracts.ContactMessageService;
import com.ape.entity.dto.request.ContactMessageRequest;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/contact-message")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping()
    @Operation(summary = "Send message to admin")
    public ResponseEntity<Response> sendMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest){
        contactMessageService.sendMessage(contactMessageRequest);
        Response response=new Response(ResponseMessage.CONTACT_MESSAGE_CREATE_RESPONSE,
                true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}























