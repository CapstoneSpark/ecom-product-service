package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    // constructor injection
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setBrand(product.getBrand());
        existing.setSku(product.getSku());
        existing.setImageUrl(product.getImageUrl());
        existing.setStock(product.getStock());

        return productRepository.save(existing);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
    
    
    
}
//
//package com.example.demo.service;
//
//import com.example.demo.entity.Product;
//import com.example.demo.repository.ProductRepository;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@Service
//public class ProductServiceImpl implements ProductService {
//
//    private final ProductRepository productRepository;
//
//    // constructor injection
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    // ============================================================
//    // CRUD OPERATIONS
//    // ============================================================
//
//    @Override
//    public Product createProduct(Product product) {
//        return productRepository.save(product);
//    }
//
//    @Override
//    public Product updateProduct(Long productId, Product product) {
//        Product existing = productRepository.findById(productId)
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.NOT_FOUND,
//                                "Product not found with id: " + productId));
//
//        existing.setName(product.getName());
//        existing.setDescription(product.getDescription());
//        existing.setPrice(product.getPrice());
//        existing.setBrand(product.getBrand());
//        existing.setSku(product.getSku());
//        existing.setImageUrl(product.getImageUrl());
//        existing.setStock(product.getStock());
//
//        return productRepository.save(existing);
//    }
//
//    @Override
//    public Product getProductById(Long productId) {
//        return productRepository.findById(productId)
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.NOT_FOUND,
//                                "Product not found with id: " + productId));
//    }
//
//    @Override
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    @Override
//    public void deleteProduct(Long productId) {
//        if (!productRepository.existsById(productId)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//                    "Product not found with id: " + productId);
//        }
//        productRepository.deleteById(productId);
//    }
//
//    // ============================================================
//    // ATOMIC STOCK OPERATIONS (Used by Order Microservice)
//    // ============================================================
//
//    @Override
//    @Transactional
//    public void reduceStockAtomic(Long productId, int qty) {
//        int updated = productRepository.decrementStock(productId, qty);
//
//        if (updated == 0) {
//            if (!productRepository.existsById(productId)) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        "Product not found");
//            }
//            throw new ResponseStatusException(HttpStatus.CONFLICT,
//                    "Not enough stock");
//        }
//    }
//
//    @Override
//    @Transactional
//    public void increaseStockAtomic(Long productId, int qty) {
//        int updated = productRepository.incrementStock(productId, qty);
//
//        if (updated == 0) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//                    "Product not found");
//        }
//    }
//}
