package com.ape.controller;

import com.ape.business.concretes.ShoppingCartManager;
import com.ape.dto.ShoppingCartDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartManager shoppingCartManager;

    @GetMapping
    @Operation(summary = "Getting cart info with cart UUID")
    public ResponseEntity<ShoppingCartDTO> getShoppingCart(@RequestHeader(value = "cartUUID",required = false) String cartUUID) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartManager.getShoppingCart(cartUUID);
        return ResponseEntity.ok(shoppingCartDTO);
    }
}
