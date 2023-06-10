package com.ape.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCountDTO {

    private long customerCount;
    private long brandCount;
    private long categoryCount;
    private long productCount;
    private long orderCount;
    private long couponCount;
    private long reviewCount;
    private long contactMessageCount;
}
