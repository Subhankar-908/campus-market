//package com.ceog.campus_marketplace.Service;
//
//import com.ceog.campus_marketplace.Dto.ProductDto;
//import com.ceog.campus_marketplace.Dto.ProductRequestDto;
//import com.ceog.campus_marketplace.Model.Category;
//import com.ceog.campus_marketplace.Model.Product;
//import com.ceog.campus_marketplace.Model.User;
//import com.ceog.campus_marketplace.Repository.CategoryRepository;
//import com.ceog.campus_marketplace.Repository.ProductRepository;
//import com.ceog.campus_marketplace.Repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    // ---------- ADD PRODUCT ----------
//    @Transactional
//    @PreAuthorize("hasRole('USER')")
//    public Product addProduct(Long userId, Long categoryId, ProductRequestDto dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found, please log in first"));
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        Product product = new Product();
//        product.setTitle(dto.getTitle());
//        product.setAmount(dto.getAmount());
//        product.setDescription(dto.getDescription());
//        product.setImgUrl(dto.getImgUrl());          // <-- String
//        product.setUploadDate(LocalDateTime.now());
//        product.setSeller(user);
//        product.setCategory(category);
//        product.setBuyId(null);
//        product.setStatus("AVAILABLE");
//
//        return productRepository.save(product);
//    }
//
//    // ---------- GET BY CATEGORY ----------
//    @PreAuthorize("hasRole('USER')")
//    public List<ProductDto> getProductByCategory(Long categoryId) {
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        return productRepository.findByCategory_Id(categoryId).stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ---------- GET ALL ----------
//    @PreAuthorize("hasRole('USER')")
//    public List<ProductDto> getAllProducts() {
//        return productRepository.findAll().stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ---------- GET BY TITLE ----------
//    @PreAuthorize("hasRole('USER')")
//    public List<ProductDto> getProductByTitle(String title) {
//        return productRepository.findByTitleContainingIgnoreCase(title).stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ---------- UPDATE PRODUCT ----------
//    @Transactional
//    @PreAuthorize("hasRole('USER')")
//    public String updateProduct(Long userId, Long productId, Product updated) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        if (!product.getSeller().getId().equals(user.getId())) {
//            throw new RuntimeException("You are not allowed to update this product");
//        }
//
//        product.setTitle(updated.getTitle());
//        product.setAmount(updated.getAmount());
//        product.setDescription(updated.getDescription());
//        product.setImgUrl(updated.getImgUrl());      // <-- String
//        product.setCategory(updated.getCategory());
//
//        productRepository.save(product);
//        return "Updated successfully";
//    }
//
//    // ---------- SELL (MARK AS SOLD) ----------
//    @Transactional
//    @PreAuthorize("hasRole('USER')")
//    public String selling(Long buyerId, Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        User buyer = userRepository.findById(buyerId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        product.setBuyId(buyer.getId());
//        product.setStatus("OUT_OF_STOCK");
//        productRepository.save(product);
//        return "Product marked as sold";
//    }
//
//    // ---------- DELETE PRODUCT ----------
//    @Transactional
//    @PreAuthorize("hasRole('USER')")
//    public void deleteProduct(Long userId, Long productId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        if (!product.getSeller().getId().equals(user.getId())) {
//            throw new RuntimeException("You are not allowed to delete this product");
//        }
//        productRepository.delete(product);
//    }
//
//    // ---------- GET PRODUCTS BOUGHT BY A USER ----------
//    @PreAuthorize("hasRole('USER')")
//    public List<ProductDto> getProductsByBuyId(Long buyId) {
//        return productRepository.findByBuyId(buyId).stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ---------- GET PRODUCTS SOLD BY A USER ----------
//    @PreAuthorize("hasRole('USER')")
//    public List<ProductDto> getProductsBySellerId(Long sellerId) {
//        return productRepository.findBySeller_Id(sellerId).stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ---------- Helper: convert Entity → DTO ----------
//    private ProductDto toDto(Product p) {
//        return ProductDto.builder()
//                .id(p.getId())
//                .title(p.getTitle())
//                .description(p.getDescription())
//                .amount(p.getAmount())
//                .imgUrl(p.getImgUrl())               // <-- already a String
//                .sellerId(p.getSeller().getId())
//                .uploadDate(p.getUploadDate())
//                .categoryName(p.getCategory().getName())
//                .status(p.getStatus())
//                .BuyerId(p.getBuyId())
//                .build();
//    }
//}

package com.ceog.campus_marketplace.Service;

import com.ceog.campus_marketplace.Dto.ProductDto;
import com.ceog.campus_marketplace.Dto.ProductFilterRequestDto;
import com.ceog.campus_marketplace.Dto.ProductRequestDto;
import com.ceog.campus_marketplace.Model.Category;
import com.ceog.campus_marketplace.Model.Product;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Repository.CategoryRepository;
import com.ceog.campus_marketplace.Repository.ProductRepository;
import com.ceog.campus_marketplace.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository  productRepository;
    private final UserRepository     userRepository;
    private final CategoryRepository categoryRepository;

    // PUBLIC — no auth needed, anyone can browse products
    public Page<ProductDto> getAllProducts(Pageable pageable, String status) {
        if (status != null && !status.isBlank()) {
            return productRepository.findByStatus(status, pageable)
                    .map(this::toDto);
        }
        return productRepository.findAll(pageable)
                .map(this::toDto);
    }

    // ── FILTER — category + college (both optional) ──────────────────────────
    public Page<ProductDto> filterProducts(ProductFilterRequestDto filter) {

        Sort sort = filter.getDirection().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                Math.min(filter.getSize(), 100),
                sort
        );

        // Default to AVAILABLE if no status provided
        String status  = StringUtils.hasText(filter.getStatus()) ? filter.getStatus() : "AVAILABLE";
        Long   catId   = filter.getCategoryId();
        String college = StringUtils.hasText(filter.getCollege()) ? filter.getCollege().trim() : null;

        if (catId != null && college != null) {
            return productRepository
                    .findByCategory_IdAndCollegeIgnoreCaseAndStatus(catId, college, status, pageable)
                    .map(this::toDto);
        }

        if (catId != null) {
            return productRepository
                    .findByCategory_IdAndStatus(catId, status, pageable)
                    .map(this::toDto);
        }

        if (college != null) {
            return productRepository
                    .findByCollegeIgnoreCaseAndStatus(college, status, pageable)
                    .map(this::toDto);
        }

        // ← THIS is the fix: always filter by status, even with no other filters
        return productRepository.findByStatus(status, pageable).map(this::toDto);
    }

    // ── GET available colleges (for dropdown) ────────────────────────────────
    public List<String> getAvailableColleges() {
        return productRepository.findAllDistinctColleges();
    }

    // ── GET colleges that have products in a specific category ───────────────
    public List<String> getCollegesByCategory(Long categoryId) {
        return productRepository.findDistinctCollegesByCategoryId(categoryId);
    }

    // PUBLIC — browse by category
    public Page<ProductDto> getProductByCategory(Long categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return productRepository.findByCategory_Id(categoryId, pageable)
                .map(this::toDto);
    }

    // PUBLIC — search by title
    public List<ProductDto> getProductByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // PROTECTED — must be logged in to add a product
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public Product addProduct(Long userId, Long categoryId, ProductRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setAmount(dto.getAmount());
        product.setDescription(dto.getDescription());
        product.setImgUrl(dto.getImgUrl());
        product.setUploadDate(LocalDateTime.now());
        product.setSeller(user);
        product.setCategory(category);
        product.setBuyId(null);
        product.setCollege(user.getCollege());
        product.setStatus("AVAILABLE");

        return productRepository.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public String updateProduct(Long userId, Long productId, Product updated) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this product");
        }

        product.setTitle(updated.getTitle());
        product.setAmount(updated.getAmount());
        product.setDescription(updated.getDescription());
        product.setImgUrl(updated.getImgUrl());

        // status was missing entirely — bug
        if (updated.getStatus() != null) {
            product.setStatus(updated.getStatus());
        }

        // category must be re-fetched from DB, not trusted from the request body
        if (updated.getCategory() != null && updated.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updated.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        productRepository.save(product);
        return "Updated successfully";
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public String selling(Long buyerId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        product.setBuyId(buyer.getId());
        product.setStatus("OUT_OF_STOCK");
        productRepository.save(product);
        return "Product marked as sold";
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void deleteProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this product");
        }
        productRepository.delete(product);
    }

    @PreAuthorize("hasRole('USER')")
    public List<ProductDto> getProductsByBuyId(Long buyId) {
        return productRepository.findByBuyId(buyId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<ProductDto> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySeller_Id(sellerId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }


private ProductDto toDto(Product product) {
    return ProductDto.builder()
            .id(product.getId())
            .title(product.getTitle())
            .description(product.getDescription())
            .amount(product.getAmount())
            .imgUrl(product.getImgUrl())
            .status(product.getStatus())
            .uploadDate(product.getUploadDate())
            .college(product.getCollege())
            // 👇 null-safe category
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
            // 👇 null-safe seller
            .sellerId(product.getSeller() != null ? product.getSeller().getId() : null)
            .sellerName(product.getSeller() != null ? product.getSeller().getUsername() : null)
            .build();
}
}