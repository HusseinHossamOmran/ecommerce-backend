package com.tccd.ecommerce_backend.controller;
import com.tccd.ecommerce_backend.dto.ProductRequest;
import com.tccd.ecommerce_backend.dto.ProductResponse;
import com.tccd.ecommerce_backend.model.Category;
import com.tccd.ecommerce_backend.model.Product;
import com.tccd.ecommerce_backend.repository.CategoryRepository;
import com.tccd.ecommerce_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController
{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest)
    {
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(()-> new RuntimeException("Category not found"));
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(new ProductResponse(saved.getId(), saved.getName(), saved.getPrice(), saved.getStock(), saved.getCategory().getId(),saved.getCategory().getName()));
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts()
    {
        List<ProductResponse> products = productRepository.findAll().stream().map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getCategory().getId(),p.getCategory().getName())).toList();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getCategory().getId(), product.getCategory().getName()));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest)
    {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        if(productRequest.getName() != null) product.setName(productRequest.getName());
        if(productRequest.getPrice() != null) product.setPrice(productRequest.getPrice());
        if(productRequest.getStock() != null) product.setStock(productRequest.getStock());
        if(productRequest.getCategoryId() != null)
        {
            Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        Product updated = productRepository.save(product);
        return ResponseEntity.ok(new ProductResponse(updated.getId(), updated.getName(), updated.getPrice(), updated.getStock(), updated.getCategory().getId(), updated.getCategory().getName()));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id)
    {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
