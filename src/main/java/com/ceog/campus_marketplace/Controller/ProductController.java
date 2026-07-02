package com.ceog.campus_marketplace.Controller;

import com.ceog.campus_marketplace.Dto.ProductDto;
import com.ceog.campus_marketplace.Dto.ProductFilterRequestDto;
import com.ceog.campus_marketplace.Dto.ProductRequestDto;
import com.ceog.campus_marketplace.Model.Product;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Repository.ProductRepository;
import com.ceog.campus_marketplace.Repository.UserRepository;
import com.ceog.campus_marketplace.Service.ProductService;
import com.ceog.campus_marketplace.WebSecurity.JwtUntil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private JwtUntil jwtUntil;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "products";  // products.html
    }

    @GetMapping
    public Page<ProductDto> listAll(
            @RequestParam(defaultValue = "0")          int page,
            @RequestParam(defaultValue = "20")         int size,
            @RequestParam(defaultValue = "uploadDate") String sortBy,   // ← was "AVAILABLE"
            @RequestParam(defaultValue = "desc")       String direction,
            @RequestParam(required = false)            String status) { // ← add status param

        List<String> allowed = List.of("uploadDate", "amount", "id", "title");
        String safeSortBy = allowed.contains(sortBy) ? sortBy : "uploadDate";

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(safeSortBy).ascending()
                : Sort.by(safeSortBy).descending();

        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sort);
        return productService.getAllProducts(pageable, status);
    }


    //add product
    @PostMapping("/addProduct/{CategoryId}")
    public ResponseEntity<Product> addProduct(
            @PathVariable Long CategoryId ,
            @Valid @RequestBody ProductRequestDto product){

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(user.getId(), CategoryId, product));
    }

    //update product buy,out of
    @PutMapping("/sell/{buyerId}/{productId}")
    public String selling(@PathVariable Long buyerId,@PathVariable Long productId ){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return productService.selling(buyerId, productId);
    }



    // GET /products/{categoryId}
    @GetMapping("/{categoryId}")
    public Page<ProductDto> getProductByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return productService.getProductByCategory(categoryId, pageable);
    }

    //get product by buyer id
    @GetMapping("/getProductBuyId/{BuyId}")
    public List<ProductDto> getProductsByBuyId(@PathVariable Long BuyId){
        System.out.println("Requested send");
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("get product");
        return productService.getProductsByBuyId(BuyId);
    }

    @GetMapping("/getProductSellId/{SellId}")
    public List<ProductDto> getProductsBySellerId(@PathVariable Long SellId){
        System.out.println("Requested send");
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("get product");
        return productService.getProductsBySellerId(SellId);
    }

    //get product by title
        @GetMapping("/getProductTitle")
        public ResponseEntity<List<ProductDto>> getProductByTitle(
                @RequestParam("title") String title) {
            List<ProductDto> products = productService.getProductByTitle(title.trim());
            return ResponseEntity.ok(products);
        }

    //update product
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> UpdateProduct(@PathVariable Long productId ,@RequestBody Product product){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.updateProduct(user.getId(), productId, product));
    }

    @GetMapping("/filter")
    public Page<ProductDto> filter(
            @RequestParam(required = false)            Long   categoryId,
            @RequestParam(required = false)            String college,
            @RequestParam(required = false)            String status,
            @RequestParam(defaultValue = "0")          int    page,
            @RequestParam(defaultValue = "20")         int    size,
            @RequestParam(defaultValue = "uploadDate") String sortBy,
            @RequestParam(defaultValue = "desc")       String direction) {

        ProductFilterRequestDto filterDto = new ProductFilterRequestDto();
        filterDto.setCategoryId(categoryId);
        filterDto.setCollege(college);
        filterDto.setStatus(status);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortBy(sortBy);
        filterDto.setDirection(direction);

        return productService.filterProducts(filterDto);
    }

    // ── GET all colleges that have available products (for dropdown)
    // GET /api/products/colleges
    @GetMapping("/colleges")
    public List<String> getAllColleges() {
        return productService.getAvailableColleges();
    }

    // ── GET colleges that have products in a specific category
    // GET /api/products/colleges?categoryId=2
    @GetMapping("/colleges/by-category")
    public List<String> getCollegesByCategory(@RequestParam Long categoryId) {
        return productService.getCollegesByCategory(categoryId);
    }

    //delete product
    @DeleteMapping("/deleteProduct/{productId}")
    public String deleteProduct(@PathVariable Long productId){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            productService.deleteProduct(user.getId(), productId);
            return "product delete successfully";
    }

}
