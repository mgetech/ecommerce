package com.example.Product.controller;


import com.example.Product.dto.ProductRequestDTO;
import com.example.Product.dto.ProductResponseDTO;
import com.example.Product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Manage product catalog and inventory")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @Operation(summary = "Get product by ID", description = "Retrieves product details by ID")
    @GetMapping("/{id}")
    public ProductResponseDTO get(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Update product", description = "Updates product details by ID")
    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO request) {
        return productService.update(id, request);
    }

    @Operation(summary = "Delete product", description = "Removes a product from the catalog by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

