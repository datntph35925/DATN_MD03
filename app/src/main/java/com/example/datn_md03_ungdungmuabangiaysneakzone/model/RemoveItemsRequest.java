package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.util.ArrayList;
import java.util.List;

public class RemoveItemsRequest {
    private List<products> products;

    public RemoveItemsRequest(List<products> products) {
        this.products = products;
    }

    public List<products> getProducts() {
        return products;
    }

    public void setProducts(List<products> products) {
        this.products = products;
    }

    public static class products {
        private List<String> productId;
        private List<Integer> size;

        public products(List<String> productId, List<Integer> size) {
            this.productId = productId;
            this.size = size;
        }

        public List<String> getProductId() {
            return productId;
        }

        public void setProductId(List<String> productId) {
            this.productId = productId;
        }

        public List<Integer> getSize() {
            return size;
        }

        public void setSize(List<Integer> size) {
            this.size = size;
        }
    }
}
