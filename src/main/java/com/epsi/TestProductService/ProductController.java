package com.epsi.TestProductService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Produits", description = "Opérations liées aux produits")
public class ProductController {

    @GetMapping()
    @Operation(summary = "Obtenir tous les produits")
    public List<Product> getProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream in = ProductController.class.getResourceAsStream("/static/product.json");
        return mapper.readValue(in,new TypeReference<List<Product>>(){});
    }

    @PostMapping()
    @Operation(summary = "Ajouter un nouveau produit")
    public String addProduct() throws IOException {
        return "Produit créé";
    }

    @DeleteMapping()
    @Operation(summary = "Supprimer un produit")
    public String deleteProduct() throws IOException {
        return "Produit supprimé";
    }

    @PutMapping()
    @Operation(summary = "Remplacer un produit")
    public String updateProduct() throws IOException {
        return "Produit remplacé en entier";
    }

    @PatchMapping()
    @Operation(summary = "Remplacer une caractéristique d'un produit")
    public String patchProduct() throws IOException {
        return "Nom du produit modifié";
    }
}