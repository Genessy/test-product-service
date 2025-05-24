package com.epsi.TestProductService.controller;

import com.epsi.TestProductService.dto.ProductCreateRequest;
import com.epsi.TestProductService.dto.ProductDto;
import com.epsi.TestProductService.dto.ProductResponse;
import com.epsi.TestProductService.entity.Product;
import com.epsi.TestProductService.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/v1/")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("products")
    @Operation(summary = "Obtenir tous les produits de la base de donnée.")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        logger.info("Obtenir tous les produits : ✅");
        List<Product> products = productService.getAllProducts();

        List<ProductResponse> responseList = products.stream().map(product -> {
            ProductResponse.ProductAttributes attributes = new ProductResponse.ProductAttributes();
            attributes.setName(product.getName());
            attributes.setDescription(product.getDescription());
            attributes.setOrigin(product.getOrigin());
            attributes.setPrice(product.getPrice());
            attributes.setStock(product.getStock());

            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setAttributes(attributes);
            return response;
        }).collect(Collectors.toList());

        Map<String, Object> jsonApi = new HashMap<>();
        jsonApi.put("data", responseList);

        return ResponseEntity.ok(jsonApi);
    }

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("products/{id}")
    @Operation(summary = "Récupère un produit par son ID.")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable String id) {
        logger.info("Récupération d’un produit par ID : 🔎 {}", id);
        Product product = productService.getProduct(id);

        ProductResponse.ProductAttributes attributes = new ProductResponse.ProductAttributes();
        attributes.setName(product.getName());
        attributes.setDescription(product.getDescription());
        attributes.setOrigin(product.getOrigin());
        attributes.setPrice(product.getPrice());
        attributes.setStock(product.getStock());

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setAttributes(attributes);

        Map<String, Object> jsonApi = new HashMap<>();
        jsonApi.put("data", response);

        return ResponseEntity.ok(jsonApi);
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PostMapping("products")
    @Operation(summary = "Ajoute un produit à la base de donnée (format JSON:API).")
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody ProductCreateRequest request) {
        ProductCreateRequest.ProductAttributes attrs = request.getData().getAttributes();

        Product product = new Product();
        product.setName(attrs.getName());
        product.setDescription(attrs.getDescription());
        product.setOrigin(attrs.getOrigin());
        product.setPrice(attrs.getPrice());
        product.setStock(attrs.getStock());

        String id = productService.addProduct(product);

        List<Product> produits = productService.getAllProducts();
        Product createdProduct = produits.stream().filter(p -> p.getName().equals(product.getName())).findFirst().orElse(null);

        ProductResponse.ProductAttributes attributes = new ProductResponse.ProductAttributes();
        attributes.setName(product.getName());
        attributes.setDescription(product.getDescription());
        attributes.setOrigin(product.getOrigin());
        attributes.setPrice(product.getPrice());
        attributes.setStock(product.getStock());

        ProductResponse response = new ProductResponse();
        response.setId(createdProduct != null ? createdProduct.getId() : "unknown");
        response.setAttributes(attributes);

        Map<String, Object> jsonApi = new HashMap<>();
        jsonApi.put("data", response);

        return ResponseEntity.status(201).body(jsonApi);
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PutMapping("products/{id}")
    @Operation(summary = "Modifie un produit dans la base de données.")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
        Product product = new Product();
        product.setId(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setOrigin(productDto.getOrigin());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        logger.info("Modification totale d'un produit : ✅ {}", product.getName());
        productService.putProduct(product);

        return ResponseEntity.status(200).build();
    }

    @Tag(name = "⚠️ Suppressions")
    @DeleteMapping("products/{id}")
    @Operation(summary = "Supprime le produit de la base de données.")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        logger.info("Suppression d'un produit : ✅ {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}