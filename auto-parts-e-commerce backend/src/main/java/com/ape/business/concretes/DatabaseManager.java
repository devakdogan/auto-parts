package com.ape.business.concretes;

import com.ape.business.abstracts.DatabaseService;
import com.ape.entity.dto.DashboardCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseManager implements DatabaseService {

    private final UserManager userManager;
    private final ProductManager productManager;
    private final CategoryManager categoryManager;
    private final BrandManager brandManager;
    private final ShoppingCartManager shoppingCartManager;

    @Override
    public DashboardCountDTO getCountOfAllRecords() {
        long userCount = userManager.countUserRecords();
        long brandCount = brandManager.countBrandRecords();
        long categoryCount = categoryManager.countCategoryRecords();
        long productCount = productManager.countProductRecords();
        DashboardCountDTO count = new DashboardCountDTO();
        count.setCustomerCount(userCount);
        count.setBrandCount(brandCount);
        count.setCategoryCount(categoryCount);
        count.setProductCount(productCount);
        return count;
    }
}
