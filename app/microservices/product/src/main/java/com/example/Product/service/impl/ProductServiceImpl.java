package com.example.Product.service.impl;


import com.example.Product.dto.ProductRequestDTO;
import com.example.Product.dto.ProductResponseDTO;
import com.example.Product.entity.Product;
import com.example.Product.exception.ProductNotFoundException;
import com.example.Product.repository.ProductRepository;
import com.example.Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO create(ProductRequestDTO request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();
        return mapToDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        return mapToDto(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    private ProductResponseDTO mapToDto(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}

