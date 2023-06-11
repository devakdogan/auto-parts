package com.ape.utility;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DiscountCalculator {

    public Double totalPriceWithDiscountCalculate(Integer quantity, Double price, Integer discount){
        return quantity*price*(100-discount)/100;
    }
}
