package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductCategoryRepository;
import com.example.demo.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-categories")
@Tag(name = "Product-Category Mapping", description = "Manage links between products and categories")
public class ProductCategoryController {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductCategoryController(ProductCategoryRepository productCategoryRepository,
                                     ProductRepository productRepository,
                                     CategoryRepository categoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    @Operation(
            summary = "Create product-category mapping",
            description = "Links a product with a specific category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mapping created successfully",
                    content = @Content(schema = @Schema(implementation = ProductCategory.class))),
            @ApiResponse(responseCode = "404", description = "Product or category not found")
    })
    @PostMapping
    public ResponseEntity<ProductCategory> createMapping(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product & Category IDs to link",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductCategoryRequest.class))
            )
            @RequestBody ProductCategoryRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));

        ProductCategory mapping = new ProductCategory(product, category);
        ProductCategory saved = productCategoryRepository.save(mapping);

        return ResponseEntity.ok(saved);
    }

    // GET ALL
    @Operation(
            summary = "Get all product-category mappings",
            description = "Returns all product-category links"
    )
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllMappings() {
        return ResponseEntity.ok(productCategoryRepository.findAll());
    }

    // GET BY ID
    @Operation(
            summary = "Get mapping by ID",
            description = "Fetch a specific product-category link"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mapping found",
                    content = @Content(schema = @Schema(implementation = ProductCategory.class))),
            @ApiResponse(responseCode = "404", description = "Mapping not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getMappingById(
            @Parameter(description = "Mapping ID", required = true)
            @PathVariable Long id) {

        ProductCategory mapping = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mapping not found: " + id));

        return ResponseEntity.ok(mapping);
    }

    // UPDATE
    @Operation(
            summary = "Update a product-category mapping",
            description = "Update the product or category of an existing mapping"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mapping updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductCategory.class))),
            @ApiResponse(responseCode = "404", description = "Mapping or resource not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateMapping(
            @Parameter(description = "Mapping ID", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated product & category IDs",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductCategoryRequest.class))
            )
            @RequestBody ProductCategoryRequest request) {

        ProductCategory existing = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mapping not found: " + id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));

        existing.setProduct(product);
        existing.setCategory(category);

        ProductCategory updated = productCategoryRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @Operation(
            summary = "Delete a mapping",
            description = "Removes a product-category link"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mapping deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Mapping not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(
            @Parameter(description = "Mapping ID", required = true)
            @PathVariable Long id) {

        productCategoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET BY PRODUCT
    @Operation(
            summary = "Get mappings by product",
            description = "Lists all category mappings for a specific product"
    )
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductCategory>> getByProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long productId) {

        return ResponseEntity.ok(productCategoryRepository.findByProductId(productId));
    }

    // GET BY CATEGORY
    @Operation(
            summary = "Get mappings by category",
            description = "Lists all product mappings for a specific category"
    )
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductCategory>> getByCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(productCategoryRepository.findByCategoryId(categoryId));
    }

    // REQUEST DTO DOCUMENTATION
    @Schema(description = "Request body for linking a product and category")
    public static class ProductCategoryRequest {

        @Schema(description = "ID of the product to link", example = "1", required = true)
        private Long productId;

        @Schema(description = "ID of the category to link", example = "3", required = true)
        private Long categoryId;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    }
}
