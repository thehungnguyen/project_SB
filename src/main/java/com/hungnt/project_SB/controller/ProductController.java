package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.entity.Product;
import com.hungnt.project_SB.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Product> saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Iterable<Product>> getAll() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(productService.getAll());
        return apiResponse;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Product> update(@PathVariable String id, @RequestBody Product product) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(productService.update(id, product));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> delete(@PathVariable String id) {
        return productService.delete(id);
    }
}
