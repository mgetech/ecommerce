package com.example.Product.service;


import com.example.Product.dto.ProductRequestDTO;
import com.example.Product.dto.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO request);
    ProductResponseDTO getById(Long id);
    ProductResponseDTO update(Long id, ProductRequestDTO request);
    void delete(Long id);
}

