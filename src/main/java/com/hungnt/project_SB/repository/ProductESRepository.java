package com.hungnt.project_SB.repository;

import com.hungnt.project_SB.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<Product, String> {
}
