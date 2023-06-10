package com.ape.business.abstracts;

import com.ape.entity.dto.DashboardCountDTO;

public interface DatabaseService {
    DashboardCountDTO getCountOfAllRecords();
}
