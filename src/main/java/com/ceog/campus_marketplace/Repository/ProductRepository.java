package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBuyId(Long buyId);
    List<Product> findBySeller_Id(Long sellerId);
    List<Product> findByTitleContainingIgnoreCase(String title);
    Optional<Product> findById(Long id);

    // Paginated versions
    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
    // Filter by category AND college  ← the main feature
    Page<Product> findByCategory_IdAndStatus(Long categoryId, String status, Pageable pageable);

    // Filter by college only
    Page<Product> findByCollegeIgnoreCaseAndStatus(String college, String status, Pageable pageable);

    // Filter by category AND college  ← the main feature
    Page<Product> findByCategory_IdAndCollegeIgnoreCaseAndStatus(
            Long categoryId, String college, String status, Pageable pageable);

    // Get all distinct colleges that have available products (for dropdown)
    @Query("SELECT DISTINCT p.college FROM Product p WHERE p.college IS NOT NULL AND p.status = 'AVAILABLE' ORDER BY p.college")
    List<String> findAllDistinctColleges();

    // Get colleges that have products in a specific category (for dynamic dropdown)
    @Query("SELECT DISTINCT p.college FROM Product p WHERE p.category.id = :categoryId AND p.college IS NOT NULL AND p.status = 'AVAILABLE' ORDER BY p.college")
    List<String> findDistinctCollegesByCategoryId(@Param("categoryId") Long categoryId);

    // Filter all products by status (used by storefront & admin panel)
    Page<Product> findByStatus(String status, Pageable pageable);
}