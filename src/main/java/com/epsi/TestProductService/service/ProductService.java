package com.epsi.TestProductService.service;

import com.epsi.TestProductService.entity.Product;
import com.epsi.TestProductService.exception.ResourceNotFoundException;
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

    public List<Product> getAllProducts() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            return documents.stream().map(doc -> {
                Product product = doc.toObject(Product.class);
                product.setId(doc.getId());
                return product;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la reception des produits", e);
        }
    }

    public Product getProduct(String id) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot document = db.collection(COLLECTION_NAME).document(id).get().get();

            if (document.exists()) {
                Product product = document.toObject(Product.class);
                product.setId(document.getId());
                return product;
            } else {
                throw new ResourceNotFoundException("Le produit avec l'ID " + id + " n'existe pas");
            }

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du produit avec l'ID " + id, e);
        }
    }

    public String addProduct(Product product) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajour d'un produit", e);
        }

    }

    public void putProduct(Product product) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(product.getId());

            // Vérifie que le produit existe avant update
            DocumentSnapshot document = docRef.get().get();
            if (!document.exists()) {
                throw new ResourceNotFoundException("Produit avec l'ID " + product.getId() + " non trouvé pour mise à jour");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("name", product.getName());
            data.put("description", product.getDescription());
            data.put("origin", product.getOrigin());
            data.put("price", product.getPrice());
            data.put("stock", product.getStock());

            docRef.set(data).get(); // .set() écrase le document existant

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du produit avec l'ID " + product.getId(), e);
        }
    }

    public void deleteProduct(String id) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);

        try {
            DocumentSnapshot document = docRef.get().get();
            if (!document.exists()) {
                throw new ResourceNotFoundException("Produit avec l'ID " + id + " non trouvé pour suppression");
            }

            docRef.delete().get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrompu", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Erreur lors de l'accès Firestore", e);
        }
    }
}
