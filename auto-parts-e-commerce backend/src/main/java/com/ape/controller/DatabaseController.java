package com.ape.controller;

import com.ape.business.abstracts.DatabaseService;
import com.ape.entity.dto.DashboardCountDTO;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get count of all sections for dashboard")
    public ResponseEntity<Response> getCountOfAllRecords(){
        DashboardCountDTO dashboardCountDTO = databaseService.getCountOfAllRecords();
        Response response = new DataResponse<>(ResponseMessage.COUNT_OF_ALL_RECORDS_RESPONSE,true,dashboardCountDTO);
        return ResponseEntity.ok(response);
    }
}
