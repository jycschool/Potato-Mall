package com.example.mysterymall.controller;

import com.example.mysterymall.po.Product;
import com.example.mysterymall.service.ProductService;
import com.example.mysterymall.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping
    public Response<List<Product>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {

        if (keyword != null && !keyword.isEmpty()) {
            if (sortBy != null && !sortBy.isEmpty()) {
                return productService.searchProductsWithSort(keyword, sortBy, sortDirection);
            }
            return productService.searchProducts(keyword);
        } else if (category != null && !category.isEmpty()) {
            if (sortBy != null && !sortBy.isEmpty()) {
                return productService.getProductsByCategoryWithSort(category, sortBy, sortDirection);
            }
            return productService.getProductsByCategory(category);
        } else {
            if (sortBy != null && !sortBy.isEmpty()) {
                return productService.getAllProductsWithSort(sortBy, sortDirection);
            }
            return productService.getAllProducts();
        }
    }

    @GetMapping("/{id}")
    public Response<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{category}")
    public Response<List<Product>> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/hot")
    public Response<List<Product>> getHotProducts(@RequestParam(defaultValue = "5") int limit) {
        return productService.getHotProducts(limit);
    }

    @GetMapping("/search")
    public Response<List<Product>> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @PostMapping
    public Response<Product> createProduct(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Response<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        product.setId(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public Response<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}