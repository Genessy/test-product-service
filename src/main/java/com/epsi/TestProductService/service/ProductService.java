package com.epsi.TestProductService.service;

import com.epsi.TestProductService.dto.ProductDto;
import com.epsi.TestProductService.entity.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final String COLLECTION_NAME = "products";

    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream().map(doc -> {
            Product product = doc.toObject(Product.class);
            product.setId(doc.getId());
            return product;
        }).collect(Collectors.toList());
    }

    public Product getProduct(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            Product product = document.toObject(Product.class);
            product.setId(document.getId());
            return product;
        } else {
            return null;
        }
    }

    public String addProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();

        Map<String, Object> data = new HashMap<>();
        data.put("name", product.getName());
        data.put("description", product.getDescription());
        data.put("origin", product.getOrigin());
        data.put("price", product.getPrice());
        data.put("stock", product.getStock());

        docRef.set(data).get();

        return "Produit bien ajouté à la base de donnée.";
    }

    public String updateProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(product.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("name", product.getName());
        data.put("description", product.getDescription());
        data.put("origin", product.getOrigin());
        data.put("price", product.getPrice());
        data.put("stock", product.getStock());

        docRef.set(data).get();

        return " Le produit à bien été complément mis à jour.";
    }

    public String patchProduct(String id, ProductDto productDto) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        DocumentSnapshot document = docRef.get().get();

        Map<String, Object> updates = new HashMap<>();

        if (productDto.getName() != null) updates.put("name", productDto.getName());
        if (productDto.getDescription() != null) updates.put("description", productDto.getDescription());
        if (productDto.getOrigin() != null) updates.put("origin", productDto.getOrigin());
        if (productDto.getPrice() != null) updates.put("price", productDto.getPrice());
        if (productDto.getStock() != null) updates.put("stock", productDto.getStock());

        if (!updates.isEmpty()) {
            docRef.update(updates).get();
        }

        DocumentSnapshot updatedSnapshot = docRef.get().get();
        Product updatedProduct = updatedSnapshot.toObject(Product.class);
        updatedProduct.setId(updatedSnapshot.getId());

        return "Le produit à bien été partiellement mis à jour.";
    }

    public String deleteProduct(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        docRef.delete();
        return "Le produit à bien été supprimé de la base de donnée.";
    }
}
