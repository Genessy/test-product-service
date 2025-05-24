package com.epsi.TestProductService.controller;

import com.epsi.TestProductService.dto.ProductDto;
import com.epsi.TestProductService.entity.Product;
import com.epsi.TestProductService.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping
    @Operation(summary = "Obtenir tous les produits de la base de donnée.")
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        logger.info("Obtenir tous les produits : ✅");
        return productService.getAllProducts();
    }

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un produit par son ID.")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        logger.info("Récupération d’un produit par ID : 🔎 {}", id);
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PostMapping
    @Operation(summary = "Ajoute un produit à la base de donnée.")
    public String addProduct(@RequestBody ProductDto productDto) throws ExecutionException, InterruptedException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setOrigin(productDto.getOrigin());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        return productService.addProduct(product);
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PutMapping("/{id}")
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

        return ResponseEntity.ok("Le produit a bien été mis à jour.");
    }

//    @Tag(name = "\uD83E\uDEB6 Écriture")
//    @PatchMapping("/{id}")
//    @Operation(summary = "Modifie partiellement un produit dans la base de donnée.")
//    public String patchProduct(@PathVariable String id, @RequestBody ProductDto productDto) throws ExecutionException, InterruptedException {
//        logger.info("Modification d'un produit : ✅ {}", productDto.getName());
//        return productService.patchProduct(id, productDto);
//    }

    @Tag(name = "⚠️ Suppressions")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime le produit de la base de données.")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        logger.info("Suppression d'un produit : ✅ {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok("Le produit a bien été supprimé de la base de données.");
    }
}