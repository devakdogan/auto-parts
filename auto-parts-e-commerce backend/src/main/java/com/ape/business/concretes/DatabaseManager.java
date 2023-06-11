package com.ape.business.concretes;

import com.ape.business.abstracts.*;
import com.ape.entity.dto.DashboardCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseManager implements DatabaseService {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final OrderService orderService;

    @Override
    public DashboardCountDTO getCountOfAllRecords() {
        long userCount = userService.countUserRecords();
        long brandCount = brandService.countBrandRecords();
        long categoryCount = categoryService.countCategoryRecords();
        long productCount = productService.countProductRecords();
        long orderCount = orderService.countOrderRecords();
        DashboardCountDTO count = new DashboardCountDTO();
        count.setCustomerCount(userCount);
        count.setBrandCount(brandCount);
        count.setCategoryCount(categoryCount);
        count.setProductCount(productCount);
        count.setOrderCount(orderCount);
        return count;
    }
}
