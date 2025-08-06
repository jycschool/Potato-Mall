package com.example.mysterymall.service.serviceImpl;

import com.example.mysterymall.dao.ProductRepository;
import com.example.mysterymall.po.Product;
import com.example.mysterymall.service.ProductService;
import com.example.mysterymall.vo.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Override
    public Response<Product> createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return Response.buildSuccess(savedProduct);
    }

    @Override
    public Response<Product> getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            return Response.buildFailure("商品不存在", "404");
        }
        return Response.buildSuccess(productOptional.get());
    }

    @Override
    public Response<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return Response.buildSuccess(products);
    }

    @Override
    public Response<List<Product>> getAllProductsWithSort(String sortBy, String sortDirection) {
        List<Product> products = productRepository.findAll();
        products = sortProducts(products, sortBy, sortDirection);
        return Response.buildSuccess(products);
    }

    @Override
    public Response<List<Product>> getHotProducts(int limit) {
        // 获取所有产品
        List<Product> allProducts = productRepository.findAll();
        
        // 如果没有产品，返回空列表
        if (allProducts.isEmpty()) {
            return Response.buildSuccess(allProducts);
        }
        
        // 从产品列表中查找名称中包含"盲盒"或"夏日"的产品，确保返回的是盲盒类商品
        List<Product> blindBoxProducts = allProducts.stream()
            .filter(product ->
                product.getName().contains("盲盒") ||
                product.getName().contains("夏日") ||
                (product.getCategory() != null && product.getCategory().contains("盲盒")))
            .limit(limit)
            .collect(Collectors.toList());
            
        // 如果找不到盲盒类商品，则返回普通商品列表
        if (blindBoxProducts.isEmpty()) {
            List<Product> regularProducts = allProducts.stream()
                .limit(limit)
                .collect(Collectors.toList());
            return Response.buildSuccess(regularProducts);
        }

        return Response.buildSuccess(blindBoxProducts);
    }

    @Override
    public Response<List<Product>> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return Response.buildSuccess(products);
    }

    @Override
    public Response<List<Product>> getProductsByCategoryWithSort(String category, String sortBy, String sortDirection) {
        List<Product> products = productRepository.findByCategory(category);
        products = sortProducts(products, sortBy, sortDirection);
        return Response.buildSuccess(products);
    }

    @Override
    public Response<List<Product>> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContaining(keyword);
        return Response.buildSuccess(products);
    }

    @Override
    public Response<List<Product>> searchProductsWithSort(String keyword, String sortBy, String sortDirection) {
        List<Product> products = productRepository.findByNameContaining(keyword);
        products = sortProducts(products, sortBy, sortDirection);
        return Response.buildSuccess(products);
    }

    @Override
    public Response<Product> updateProduct(Product product) {
        Optional<Product> existingProductOptional = productRepository.findById(product.getId());
        if (!existingProductOptional.isPresent()) {
            return Response.buildFailure("商品不存在", "404");
        }
        Product updatedProduct = productRepository.save(product);
        return Response.buildSuccess(updatedProduct);
    }

    @Override
    public Response<String> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return Response.buildFailure("商品不存在", "404");
        }
        productRepository.deleteById(id);
        return Response.buildSuccess("删除成功");
    }

    // 辅助方法：根据指定的排序字段和排序方向对商品列表进行排序
    private List<Product> sortProducts(List<Product> products, String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.isEmpty()) {
            return products;
        }

        boolean isAsc = !"desc".equalsIgnoreCase(sortDirection);

        Comparator<Product> comparator = null;

        switch(sortBy) {
            case "price":
                comparator = Comparator.comparing(Product::getPrice);
                break;
            case "created":
                // 使用商品ID替代创建时间进行排序，通常ID越大表示越新创建的商品
                comparator = Comparator.comparing(Product::getId);
                break;
            default:
                return products;
        }

        if (!isAsc) {
            comparator = comparator.reversed();
        }

        return products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}