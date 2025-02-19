package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.entity.Product;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.ProductESRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductESRepository productESRepository;

    public ApiResponse<Product> saveProduct(Product product) {
        Product productInES = productESRepository.save(product);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(productInES);

        return apiResponse;
    }

    public ApiResponse<Iterable<Product>> getAll() {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setResult(productESRepository.findAll());

        return apiResponse;
    }

    public ApiResponse<Product> update(String id, Product product) {
        Product p = productESRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        p.setName(product.getName());
        p.setDescription(product.getDescription());
        p.setPrice(product.getPrice());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(productESRepository.save(p));
        return apiResponse;
    }

    public ApiResponse<String> delete(String id) {
        productESRepository.deleteById(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("Successfully Deleted");
        return apiResponse;
    }
}
