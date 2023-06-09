package com.ape.controller;

import com.ape.business.concretes.ProductManager;
import com.ape.entity.dto.ProductDTO;
import com.ape.entity.dto.request.ProductRequest;
import com.ape.entity.dto.request.ProductUpdateRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import com.ape.entity.enums.ProductStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductManager productManager;

    @GetMapping
    @Operation(summary = "Get all products with filter and page")
    public ResponseEntity<PageImpl<ProductDTO>> getAllWithQueryAndPage(@RequestParam(value = "q",required = false) String query,
                                                            @RequestParam(value = "categories",required = false) List<Long> categoryId,
                                                            @RequestParam(value = "brands",required = false) List<Long> brandId,
                                                            @RequestParam(value = "minPrice",required = false) Integer minPrice,
                                                            @RequestParam(value = "maxPrice",required = false) Integer maxPrice,
                                                            @RequestParam(value = "status",required = false) ProductStatus status,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sort") String prop,
                                                            @RequestParam(value = "direction",
                                                                    required = false,
                                                                    defaultValue = "DESC") Sort.Direction direction){
        Pageable pageable = PageRequest.of(page,size, Sort.by(direction,prop));
        PageImpl<ProductDTO> productDTO = productManager.getAllWithQueryAndPage(query,categoryId,brandId,minPrice,maxPrice,status,pageable);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product with ID")
    public ResponseEntity<ProductDTO> getProductWithId(@PathVariable("productId") Long id){
        ProductDTO response = productManager.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product in database")
    public ResponseEntity<Response> saveProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductDTO productDTO = productManager.saveProduct(productRequest);
        Response response = new DataResponse<>(ResponseMessage.PRODUCT_SAVED_RESPONSE_MESSAGE,true,productDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product with ID")
    public ResponseEntity<Response> updateProduct(@PathVariable("productId") Long id,
                                                  @Valid @RequestBody ProductUpdateRequest productUpdateRequest){
        ProductDTO productDTO = productManager.updateProduct(id, productUpdateRequest);
        DataResponse<ProductDTO> response = new DataResponse<>(ResponseMessage.PRODUCT_UPDATED_RESPONSE_MESSAGE,true,productDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/admin")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Delete product with ID")
    public ResponseEntity<Response> deleteProduct(@PathVariable("productId") Long id){
        ProductDTO productDTO = productManager.removeById(id);
        Response response = new DataResponse<>(ResponseMessage.PRODUCT_DELETE_RESPONSE_MESSAGE,true,productDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/image/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product image with image ID")
    public ResponseEntity<Response>deleteProductImage(@PathVariable("imageId") String id){
        productManager.removeProductImageByImageId(id);
        Response response =new Response(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

}
