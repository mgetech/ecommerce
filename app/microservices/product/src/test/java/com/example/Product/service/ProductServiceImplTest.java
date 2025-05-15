package com.example.Product.service;

import com.example.Product.entity.Product;
import com.example.Product.repository.ProductRepository;
import com.example.Product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Rollback(value = false)
public class ProductServiceImplTest {

    @Autowired
    ProductServiceImpl productServiceImp;
    @Autowired
    ProductRepository productRepository;

/*    @BeforeEach
    public void setup() {
        productRepository.save(new Product("Book", "A great book", 12.99));
        productRepository.save(new Product("Laptop", "A fast laptop", 899.00));
        productRepository.save(new Product("Smartphone", "A high-end smartphone", 1099.00));
    }*/


    @Test
    public void getByIdTest(){
        System.out.println(productServiceImp.getById(2L));
    }

}
