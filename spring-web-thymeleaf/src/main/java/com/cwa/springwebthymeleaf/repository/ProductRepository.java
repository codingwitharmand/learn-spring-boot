package com.cwa.springwebthymeleaf.repository;

import com.cwa.springwebthymeleaf.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
