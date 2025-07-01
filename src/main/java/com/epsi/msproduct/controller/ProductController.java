package com.epsi.msproduct.controller;

import com.epsi.msproduct.dto.common.ProductCreateRequest;
import com.epsi.msproduct.dto.common.ProductDto;
import com.epsi.msproduct.dto.common.ProductResponse;
import com.epsi.msproduct.entity.Product;
import com.epsi.msproduct.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("products")
    @Operation(summary = "Obtenir tous les produits de la base de donnée.")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        logger.info("Obtention de tous les produits.");
        List<Product> products = productService.getAllProducts();

        List<ProductResponse> responseList = products.stream().map(product -> {
            ProductResponse.ProductAttributes attributes = new ProductResponse.ProductAttributes();
            attributes.setName(product.getName());
            attributes.setDescription(product.getDescription());
            attributes.setOrigin(product.getOrigin());
            attributes.setPrice(product.getPrice());
            attributes.setStock(product.getStock());
            attributes.setTag(product.getTag());

            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setAttributes(attributes);
            return response;
        }).toList();

        Map<String, Object> jsonApi = new HashMap<>();
        jsonApi.put("data", responseList);

        return ResponseEntity.ok(jsonApi);
    }

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("products/{id}")
    @Operation(summary = "Récupère un produit par son ID.")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable String id) {
        logger.info("Récupération d’un produit par son ID : {}.", id);
        Product product = productService.getProduct(id);

        ProductResponse.ProductAttributes attributes = new ProductResponse.ProductAttributes();
        attributes.setName(product.getName());
        attributes.setDescription(product.getDescription());
        attributes.setOrigin(product.getOrigin());
        attributes.setPrice(product.getPrice());
        attributes.setStock(product.getStock());
        attributes.setTag(product.getTag());

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
        logger.info("Ajout d’un produit à la base de donnée.");
        ProductCreateRequest.ProductAttributes attrs = request.getData().getAttributes();

        Product product = new Product();
        product.setName(attrs.getName());
        product.setDescription(attrs.getDescription());
        product.setOrigin(attrs.getOrigin());
        product.setPrice(attrs.getPrice());
        product.setStock(attrs.getStock());
        product.setTag(attrs.getTag());

        String id = productService.addProduct(product);

        ProductResponse.ProductAttributes responseAttrs = new ProductResponse.ProductAttributes();
        responseAttrs.setName(product.getName());
        responseAttrs.setDescription(product.getDescription());
        responseAttrs.setOrigin(product.getOrigin());
        responseAttrs.setPrice(product.getPrice());
        responseAttrs.setStock(product.getStock());
        responseAttrs.setTag(product.getTag());

        ProductResponse response = new ProductResponse();
        response.setId(id);
        response.setAttributes(responseAttrs);

        Map<String, Object> jsonApi = new HashMap<>();
        jsonApi.put("data", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(jsonApi);
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
        product.setTag(productDto.getTag());

        logger.info("Modification d'un produit : {}.", product.getName());
        productService.putProduct(product);

        return ResponseEntity.status(200).build();
    }

    @Tag(name = "⚠️ Suppressions")
    @DeleteMapping("products/{id}")
    @Operation(summary = "Supprime le produit de la base de données.")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        logger.info("Suppression d'un produit par son ID : {}.", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}