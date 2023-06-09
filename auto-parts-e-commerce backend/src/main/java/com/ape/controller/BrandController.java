package com.ape.controller;

import com.ape.business.concretes.BrandManager;
import com.ape.entity.dto.BrandDTO;
import com.ape.entity.dto.request.BrandRequest;
import com.ape.entity.dto.request.BrandUpdateRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import com.ape.entity.enums.BrandStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {

    private final BrandManager brandManager;

    @GetMapping
    @Operation(summary = "Get all brands with filter and page")
    public ResponseEntity<PageImpl<BrandDTO>> getAllBrandsWithFilterAndPage(@RequestParam(value = "q",required = false)String query,
                                                           @RequestParam(value = "status",required = false) BrandStatus status,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("sort") String prop,
                                                           @RequestParam(value="direction",required = false,
                                                                   defaultValue = "DESC") Sort.Direction direction){
        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        PageImpl<BrandDTO> brandDTOPage=brandManager.getAllBrandsWithFilterAndPage(query,status,pageable);
        return ResponseEntity.ok(brandDTOPage);
    }

    @GetMapping("/{brandId}")
    @Operation(summary = "Get brand by ID")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable("brandId") Long id){
        BrandDTO brandDTO = brandManager.getBrandById(id);
        return ResponseEntity.ok(brandDTO);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a brand in database")
    public  ResponseEntity<Response> createBrand(@Valid @RequestBody BrandRequest brandRequest){
        BrandDTO brandDTO = brandManager.createBrand(brandRequest);
        Response response = new DataResponse<>(ResponseMessage.BRAND_CREATE_RESPONSE_MESSAGE,true,brandDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update brand with ID")
    public ResponseEntity<Response> updateBrand(@Valid @PathVariable("brandId") Long id,@RequestBody BrandUpdateRequest brandUpdateRequest){
        BrandDTO brandDTO = brandManager.updateBrand(id,brandUpdateRequest);
        Response response = new DataResponse<>(ResponseMessage.BRAND_UPDATE_RESPONSE_MESSAGE,true,brandDTO);
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete brand with ID")
    public ResponseEntity<Response> deleteBrandById(@PathVariable("brandId") Long id){
        BrandDTO brandDTO = brandManager.deleteBrandById(id);
        Response response = new DataResponse<>(ResponseMessage.BRAND_DELETE_RESPONSE_MESSAGE,true,brandDTO);
        return ResponseEntity.ok(response);
    }
}
