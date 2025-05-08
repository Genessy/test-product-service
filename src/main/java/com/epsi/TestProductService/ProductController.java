package com.epsi.TestProductService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping()
    public List<Product> getProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream in = ProductController.class.getResourceAsStream("/static/product.json");
        return mapper.readValue(in,new TypeReference<List<Product>>(){});
    }

    @PostMapping()
    public String addProduct() throws IOException {
        return "Produit créé";
    }

    @DeleteMapping()
    public String deleteProduct() throws IOException {
        return "Produit supprimé";
    }

    @PutMapping()
    public String updateProduct() throws IOException {
        return "Produit remplacé en entier";
    }

    @PatchMapping()
    public String patchProduct() throws IOException {
        return "Nom du produit modifié";
    }
}