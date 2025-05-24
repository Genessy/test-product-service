package com.epsi.TestProductService.dto;

public class ProductCreateRequest {
    private ProductData data;

    public ProductData getData() {
        return data;
    }

    public void setData(ProductData data) {
        this.data = data;
    }

    public static class ProductData {
        private String type;
        private ProductAttributes attributes;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ProductAttributes getAttributes() {
            return attributes;
        }

        public void setAttributes(ProductAttributes attributes) {
            this.attributes = attributes;
        }
    }

    public static class ProductAttributes {
        private String name;
        private String description;
        private String origin;
        private double price;
        private int stock;

        // Getters & Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getOrigin() { return origin; }
        public void setOrigin(String origin) { this.origin = origin; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }
}