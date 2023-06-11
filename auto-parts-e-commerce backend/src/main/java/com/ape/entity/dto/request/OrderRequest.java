package com.ape.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank
    private String contactName;
    @NotBlank
    private String phoneNumber;
    @NotNull
    private Long shippingAddressId;
    @NotNull
    private Long invoiceAddressId;
    private boolean saveCart;
    @NotBlank
    private String nameOnCard;
    @NotBlank
    private String cardNo;
    @NotBlank
    private String expireDate;
    @NotBlank
    private String cvc;
    private String title;
}
