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

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping
    @Operation(summary = "Obtenir tous les produits de la base de donnée.")
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        return productService.getAllProducts();
    }

    @Tag(name = "\uD83D\uDD0D Lecture")
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un produit via son id depuis la base de donnée.")
    public Product getProduct(@PathVariable String id) throws ExecutionException, InterruptedException {
        return productService.getProduct(id);
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
    @Operation(summary = "Modifie un produit dans la base de donnée.")
    public String updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) throws ExecutionException, InterruptedException {
        Product product = new Product();
        product.setId(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setOrigin(productDto.getOrigin());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        return productService.updateProduct(product);
    }

    @Tag(name = "\uD83E\uDEB6 Écriture")
    @PatchMapping("/{id}")
    @Operation(summary = "Modifie partiellement un produit dans la base de donnée.")
    public String patchProduct(@PathVariable String id, @RequestBody ProductDto productDto)
            throws ExecutionException, InterruptedException {
        return productService.patchProduct(id, productDto);
    }

    @Tag(name = "⚠\uFE0F Suppressions")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime le produit de la base de donnée.")
    public String deleteProduct(@PathVariable String id) throws ExecutionException, InterruptedException {
        return productService.deleteProduct(id);
    }
}