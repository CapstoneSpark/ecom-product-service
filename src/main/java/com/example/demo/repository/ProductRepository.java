package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%'))")
    List<Product> searchByKeyword(@Param("kw") String keyword);
    Optional<Product> findBySku(String sku);
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock - :qty WHERE p.productId = :id AND p.stock >= :qty")
    int decrementStock(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock + :qty WHERE p.productId = :id")
    int incrementStock(@Param("id") Long id, @Param("qty") int qty);


}
