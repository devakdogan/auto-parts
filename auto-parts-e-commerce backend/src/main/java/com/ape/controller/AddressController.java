package com.ape.controller;

import com.ape.business.abstracts.AddressService;
import com.ape.entity.dto.AddressDTO;
import com.ape.entity.dto.request.AddressRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Getting authenticated user's all addresses")
    public ResponseEntity<List<AddressDTO>> GetAuthUserAllAddresses(){
        List<AddressDTO> addressDTOList = addressService.getAuthUserAllAddresses();
        return ResponseEntity.ok(addressDTOList);
    }

    @GetMapping("/{addressId}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Getting authenticated user address with ID")
    public ResponseEntity<AddressDTO> getAuthUserAddressById(@PathVariable("addressId") Long id){
        AddressDTO addressDTOList = addressService.getAuthUserAddressesById(id);
        return ResponseEntity.ok(addressDTOList);
    }

    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Authenticated user create an address in database")
    public ResponseEntity<Response> saveAddress(@Valid @RequestBody AddressRequest addressRequest) {
        AddressDTO userAddressDTO = addressService.saveAddress(addressRequest);
        Response response = new DataResponse<>(ResponseMessage.ADDRESS_CREATED_RESPONSE_MESSAGE, true,userAddressDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Authenticated user update an address with ID")
    public ResponseEntity<Response> updateAuthUserAddresses(@PathVariable("addressId") Long id, @Valid @RequestBody AddressRequest addressRequest){
        AddressDTO addressDTO = addressService.updateAuthUserAddresses(id,addressRequest);
        Response response = new DataResponse<>(ResponseMessage.ADDRESS_UPDATE_RESPONSE_MESSAGE,true,addressDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Authenticated user remove an address with ID")
    public ResponseEntity<Response> removeAddressAuth(@PathVariable("addressId") Long id){
        addressService.removeAddressById(id);
        Response response = new Response(ResponseMessage.ADDRESS_DELETE_RESPONSE_MESSAGE, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin getting all addresses with userId")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByUserId(@PathVariable("userId") Long userId){
        List<AddressDTO> response = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{addressId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin getting address with ID")
    public ResponseEntity<AddressDTO> adminGetAddressById(@PathVariable("addressId") Long id){
        AddressDTO response = addressService.adminGetAddressById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{addressId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin update an address with ID")
    public ResponseEntity<AddressDTO> adminUpdateAddress(@PathVariable("addressId") Long id, @Valid @RequestBody AddressRequest addressRequest){
        AddressDTO response = addressService.adminUpdateAddress(id,addressRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin delete an address with ID")
    public ResponseEntity<Response> adminDeleteUserAddress(@PathVariable("addressId") Long id){
        addressService.removeAdminUserAddressById(id);
        Response response = new Response(ResponseMessage.ADDRESS_DELETE_RESPONSE_MESSAGE, true);
        return ResponseEntity.ok(response);
    }
}
