package com.example.mysterymall.service;

import com.example.mysterymall.po.Product;
import com.example.mysterymall.vo.Response;

import java.util.List;

public interface ProductService {
    Response<Product> createProduct(Product product);
    Response<Product> getProductById(Long id);
    Response<List<Product>> getAllProducts();
    Response<List<Product>> getAllProductsWithSort(String sortBy, String sortDirection);
    Response<List<Product>> getProductsByCategory(String category);
    Response<List<Product>> getProductsByCategoryWithSort(String category, String sortBy, String sortDirection);
    Response<List<Product>> searchProducts(String keyword);
    Response<List<Product>> searchProductsWithSort(String keyword, String sortBy, String sortDirection);
    Response<Product> updateProduct(Product product);
    Response<String> deleteProduct(Long id);
    Response<List<Product>> getHotProducts(int limit);
}