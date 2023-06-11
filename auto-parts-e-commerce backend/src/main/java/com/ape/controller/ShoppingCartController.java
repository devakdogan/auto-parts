package com.ape.controller;

import com.ape.business.abstracts.ShoppingCartService;
import com.ape.entity.dto.ShoppingCartDTO;
import com.ape.entity.dto.ShoppingCartItemDTO;
import com.ape.entity.dto.request.ShoppingCartRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Getting cart info with cart UUID")
    public ResponseEntity<ShoppingCartDTO> getShoppingCart(@RequestHeader(value = "cartUUID",required = false) String cartUUID) {
        ShoppingCartDTO response = shoppingCartService.getShoppingCart(cartUUID);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a shopping cart item to add shopping cart")
    public ResponseEntity <Response> createCartItem(@RequestHeader("cartUUID") String cartUUID, @RequestBody ShoppingCartRequest shoppingCartRequest) {
        ShoppingCartItemDTO shoppingCartItemDTO = shoppingCartService.createCartItem(cartUUID,shoppingCartRequest);
        Response response = new DataResponse<>(ResponseMessage.ITEM_ADDED_RESPONSE_MESSAGE,true,shoppingCartItemDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/{op}")
    @Operation(summary = "Change shopping cart item quantity in shopping cart")
    public ResponseEntity<ShoppingCartItemDTO> changeQuantity(@RequestHeader("cartUUID") String cartUUID,@PathVariable("productId") Long productId , @PathVariable("op") String op){
        ShoppingCartItemDTO updatedItem = shoppingCartService.changeItemQuantity(cartUUID,productId, op);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("{productId}")
    @Operation(summary = "Delete shopping cart item with ID")
    public ResponseEntity<Response> deleteCartItem(@RequestHeader("cartUUID") String cartUUID,@PathVariable("productId") Long productId) {
        ShoppingCartItemDTO shoppingCartItemDTO = shoppingCartService.removeCartItem(cartUUID,productId);
        Response response = new DataResponse<>(ResponseMessage.CART_ITEM_DELETED_RESPONSE_MESSAGE, true, shoppingCartItemDTO);
        return ResponseEntity.ok(response);

    }
}
