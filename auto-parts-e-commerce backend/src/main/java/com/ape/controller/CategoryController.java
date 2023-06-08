package com.ape.controller;

import com.ape.business.concretes.CategoryManager;
import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.dto.request.CategoryUpdateRequest;
import com.ape.dto.response.DataResponse;
import com.ape.dto.response.Response;
import com.ape.dto.response.ResponseMessage;
import com.ape.entity.enums.CategoryStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/category")
public class CategoryController {

    private final CategoryManager categoryManager;

    @GetMapping()
    @Operation(summary = "Get all categories with filter and page")
    public ResponseEntity<Page<CategoryDTO>> getAllCategoriesWithFilterAndPage(@RequestParam(value = "q",required = false)String query,
                                                                      @RequestParam(value = "status",required = false) CategoryStatus status,
                                                                      @RequestParam("page") int page,
                                                                      @RequestParam("size") int size, @RequestParam("sort") String prop,
                                                                      @RequestParam(value = "direction", required = false, defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction,prop));
        Page<CategoryDTO> response = categoryManager.getAllCategoriesWithFilterAndPage(query,status,pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/option")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get category list for dropdown menu in frontend")
    public ResponseEntity<List<CategoryDTO>> getAllCategoryForOption(){
        return ResponseEntity.ok(categoryManager.getAllCategoryList());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a category in database")
    public ResponseEntity<Response> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO= categoryManager.saveCategory(categoryRequest);
        Response response = new DataResponse<>(ResponseMessage.CATEGORY_CREATED_RESPONSE_MESSAGE, true,categoryDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category with ID")
    public ResponseEntity<Response> updateCategory(@PathVariable("categoryId") Long id,
                                                      @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        CategoryDTO categoryDTO = categoryManager.updateCategory(id,categoryUpdateRequest);
        Response response = new DataResponse<>(ResponseMessage.CATEGORY_UPDATED_RESPONSE_MESSAGE, true,categoryDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove category with ID")
    public ResponseEntity<Response> removeCategory(@PathVariable("categoryId") Long id){
        CategoryDTO categoryDTO = categoryManager.removeById(id);
        Response response = new DataResponse<>(ResponseMessage.CATEGORY_DELETED_RESPONSE_MESSAGE, true,categoryDTO);
        return ResponseEntity.ok(response);
    }
}
