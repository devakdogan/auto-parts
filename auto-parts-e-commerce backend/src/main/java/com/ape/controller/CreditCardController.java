package com.ape.controller;

import com.ape.business.abstracts.CreditCardService;
import com.ape.entity.dto.CreditCardDTO;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("credit-card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @GetMapping("/option")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get auth user payment list for dropdown menu in frontend")
    public ResponseEntity<List<CreditCardDTO>> getAllPaymentInfoOfUserOption(){
        return ResponseEntity.ok(creditCardService.getAllPaymentInfoForAuthUser());
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get credit card info with ID")
    public ResponseEntity<CreditCardDTO> getCreditCardInfoWithId(@RequestParam("creditCardId") Long creditCardId){
        return ResponseEntity.ok(creditCardService.getAuthUserCreditCardInfoWithID(creditCardId));
    }

}
