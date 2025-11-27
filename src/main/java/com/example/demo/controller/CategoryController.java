package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

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
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Manage product categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    @Operation(
            summary = "Create a new category",
            description = "Adds a new category to the product catalog"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Category.class))
            )
            @RequestBody Category category) {

        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(saved);
    }

    // GET ALL
    @Operation(
            summary = "Get all categories",
            description = "Fetch all categories stored in the system"
    )
    @ApiResponse(responseCode = "200", description = "List of categories returned successfully")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    // GET BY ID
    @Operation(
            summary = "Get category by ID",
            description = "Fetch a single category using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID of the category to fetch", required = true)
            @PathVariable Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return ResponseEntity.ok(category);
    }

    // UPDATE
    @Operation(
            summary = "Update category",
            description = "Modify the details of an existing category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated category details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Category.class))
            )
            @RequestBody Category updated) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());

        Category saved = categoryRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // DELETE
    @Operation(
            summary = "Delete category",
            description = "Delete a category by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true)
            @PathVariable Long id) {

        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
