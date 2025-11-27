package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductCategory;
import com.example.demo.repository.ProductCategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService,
                             ProductCategoryRepository productCategoryRepository,
                             ProductRepository productRepository) {
        this.productService = productService;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    // CREATE PRODUCT
    @Operation(
            summary = "Create a new product",
            description = "Adds a new product to the database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Product.class))
            )
            @RequestBody Product product) {

        Product saved = productService.createProduct(product);
        return ResponseEntity.ok(saved);
    }

    // GET ALL PRODUCTS (PAGINATED)
    @Operation(
            summary = "Get all products (paginated)",
            description = "Retrieves a paginated list of all products"
    )
    @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Pagination parameters") Pageable pageable) {

        Page<Product> page = productRepository.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    // GET PRODUCT BY ID
    @Operation(
            summary = "Get product by ID",
            description = "Fetch a single product using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {

        return ResponseEntity.ok(productService.getProductById(productId));
    }

    // UPDATE PRODUCT
    @Operation(
            summary = "Update product details",
            description = "Updates an existing product using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long productId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated product details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Product.class))
            )
            @RequestBody Product product) {

        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    // DELETE PRODUCT
    @Operation(
            summary = "Delete product",
            description = "Deletes a product from the database using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long productId) {

        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // SEARCH PRODUCTS
    @Operation(
            summary = "Search products",
            description = "Search products by keyword (name, description, etc.)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Search keyword", required = true, example = "Laptop")
            @RequestParam("q") String keyword) {

        return ResponseEntity.ok(productRepository.searchByKeyword(keyword));
    }

    // GET PRODUCTS BY CATEGORY
    @Operation(
            summary = "Get products by category",
            description = "Returns a list of products belonging to a specific category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products returned successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long categoryId) {

        List<ProductCategory> mappings = productCategoryRepository.findByCategoryId(categoryId);
        List<Product> products = mappings.stream()
                .map(ProductCategory::getProduct)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }
    
 // GET PRODUCT BY SKU
    @Operation(
            summary = "Get product by SKU",
            description = "Fetch a single product using its SKU"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getProductBySku(
            @Parameter(description = "SKU of the product", required = true)
            @PathVariable String sku) {

        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found with SKU: " + sku));

        return ResponseEntity.ok(product);
    }

    
    
}
