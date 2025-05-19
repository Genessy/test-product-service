package com.epsi.TestProductService.controller;

import com.epsi.TestProductService.dto.ProductDto;
import com.epsi.TestProductService.entity.Product;
import com.epsi.TestProductService.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            var resultGetAllProducts = productService.getAllProducts();
            logger.info("Obtention de tous les produits : ✅");
            return resultGetAllProducts;
        } catch ( Exception e ) {
            logger.error("Obtention de tous les produits : ❌ {}", e.getMessage());
            return null;
        }
    }

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un produit via son id depuis la base de donnée.")
    public Product getProduct(@PathVariable String id) throws ExecutionException, InterruptedException {
        try {
            var resultGetProduct = productService.getProduct(id);
            logger.info("Obtention d'un produit : ✅");
            return resultGetProduct;
        } catch (Exception e) {
            logger.error("Obtention d'un produit : ❌ {}",e.getMessage());
            return null;
        }
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PostMapping
    @Operation(summary = "Ajoute un produit à la base de donnée.")
    public String addProduct(@RequestBody ProductDto productDto) throws ExecutionException, InterruptedException {
        try {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setOrigin(productDto.getOrigin());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            var resultAddProduct = productService.addProduct(product);
            logger.info("Création d'un produit : ✅ {}", product.getName());
            return resultAddProduct;
        } catch ( Exception e ) {
            logger.error("Création d'un produit : ❌ {}", e.getMessage());
            return null;
        }
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PutMapping("/{id}")
    @Operation(summary = "Modifie un produit dans la base de donnée.")
    public String updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) throws ExecutionException, InterruptedException {
        try {
            Product product = new Product();
            product.setId(id);
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setOrigin(productDto.getOrigin());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            var resultUpdateProduct = productService.updateProduct(product);
            logger.info("Modification totale d'un produit : ✅ {}", product.getName());
            return resultUpdateProduct;
        } catch (Exception e) {
            logger.error("Modification totale d'un produit : ❌ {}", e.getMessage());
            return null;
        }
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PatchMapping("/{id}")
    @Operation(summary = "Modifie partiellement un produit dans la base de donnée.")
    public String patchProduct(@PathVariable String id, @RequestBody ProductDto productDto)
            throws ExecutionException, InterruptedException {
        try {
            var resultPatchProduct = productService.patchProduct(id, productDto);
            logger.info("Modification d'un produit : ✅ {}", productDto.getName());
            return resultPatchProduct;
        } catch (Exception e) {
            logger.error("Modification d'un produit : ❌ {}", e.getMessage());
            return null;
        }

    }

    @Tag(name = "⚠\uFE0F Suppressions")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime le produit de la base de donnée.")
    public String deleteProduct(@PathVariable String id) throws ExecutionException, InterruptedException {
        try {
            var resultDeleteProduct = productService.deleteProduct(id);
            logger.info("Suppression d'un produit : ✅ {}", id);
            return resultDeleteProduct;
        } catch (Exception e) {
            logger.error("Suppression d'un produit : {}", e.getMessage());
            return null;
        }
    }
}