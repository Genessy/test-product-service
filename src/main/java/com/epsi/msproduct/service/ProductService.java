package com.epsi.msproduct.service;

import com.epsi.msproduct.dto.order.common.AvailableProductDto;
import com.epsi.msproduct.dto.order.common.WishedAndAvailableProductDto;
import com.epsi.msproduct.dto.order.common.WishedProductDto;
import com.epsi.msproduct.dto.order.incoming.OrderMessageDto;
import com.epsi.msproduct.dto.order.outgoing.ProductOrderResponseDto;
import com.epsi.msproduct.entity.Product;
import com.epsi.msproduct.exception.ResourceNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
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
            data.put("tag", product.getTag());

            docRef.set(data).get();

            return docRef.getId();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout d'un produit", e);
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
            data.put("tag", product.getTag());

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

    public ProductOrderResponseDto handleOrderCheck(OrderMessageDto order) {
        Firestore db = FirestoreClient.getFirestore();

        List<AvailableProductDto> ordered = new ArrayList<>();
        List<WishedAndAvailableProductDto> partiallyOrdered = new ArrayList<>();
        List<WishedAndAvailableProductDto> notAvailable = new ArrayList<>();

        for (WishedProductDto item : order.getProducts()) {
            DocumentSnapshot doc;
            try {
                doc = db.collection(COLLECTION_NAME).document(item.getProductId()).get().get();
            } catch (Exception e) {
                System.out.println("Erreur Firestore : " + e.getMessage());
                notAvailable.add(wishedOnly(item));
                continue;
            }

            if (!doc.exists()) {
                System.out.println("Produit non trouvé : " + item.getProductId());
                notAvailable.add(wishedOnly(item));
                continue;
            }

            Product product = doc.toObject(Product.class);
            product.setId(doc.getId());

            int requestedQty;
            try {
                requestedQty = Integer.parseInt(item.getQuantity());
            } catch (Exception e) {
                System.out.println("Quantité invalide pour produit " + item.getProductId());
                notAvailable.add(wishedOnly(item));
                continue;
            }

            int stock = product.getStock();
            List<AvailableProductDto> suggestions = new ArrayList<>();

            if (stock >= requestedQty) {

                ordered.add(new AvailableProductDto(product.getId(), product.getName()));
            } else {
                int missingQty = requestedQty - stock;

                if (product.getTag() != null) {
                    try {
                        QuerySnapshot similarDocs = db.collection(COLLECTION_NAME)
                                .whereEqualTo("tag", product.getTag())
                                .get().get();

                        for (DocumentSnapshot similarDoc : similarDocs.getDocuments()) {
                            Product similar = similarDoc.toObject(Product.class);
                            similar.setId(similarDoc.getId());
                            if (similar.getId().equals(product.getId())) continue;
                            if (similar.getStock() <= 0) continue;

                            suggestions.add(new AvailableProductDto(similar.getId(), similar.getName()));
                            missingQty -= similar.getStock();

                            if (missingQty <= 0) break;
                        }
                    } catch (Exception e) {
                        System.out.println("Erreur produits similaires : " + e.getMessage());
                    }
                }

                WishedAndAvailableProductDto dto = new WishedAndAvailableProductDto();
                dto.setWishedProduct(List.of(item));
                dto.setAvailableProduct(suggestions);

                if (suggestions.isEmpty() || missingQty > 0) {
                    notAvailable.add(dto);
                } else {
                    partiallyOrdered.add(dto);
                }
            }
        }

        String status;
        if (!notAvailable.isEmpty()) {
            status = "FAILED";
        } else if (!partiallyOrdered.isEmpty()) {
            status = "PARTIALLY";
        } else {
            status = "OK";
        }

        ProductOrderResponseDto response = new ProductOrderResponseDto();
        response.setOrderId(order.getOrderId());
        response.setStatus(status);
        response.setOrdered(ordered);
        response.setPartiallyOrdered(partiallyOrdered);
        response.setNotAvailable(notAvailable);

        return response;
    }

    private WishedAndAvailableProductDto wishedOnly(WishedProductDto wished) {
        WishedAndAvailableProductDto dto = new WishedAndAvailableProductDto();
        dto.setWishedProduct(List.of(wished));
        dto.setAvailableProduct(List.of());
        return dto;
    }
}
