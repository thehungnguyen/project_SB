package com.hungnt.project_SB.service;

import com.hungnt.project_SB.entity.Product;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.ProductESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductESRepository productESRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Product saveProduct(Product product){ return productESRepository.save(product);}

    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Product> getAll(){
        return productESRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Product update(String id, Product product){
        Product p = productESRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        p.setName(product.getName());
        p.setDescription(product.getDescription());
        p.setPrice(product.getPrice());

        return productESRepository.save(p);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id){
        productESRepository.deleteById(id);
    }
}
