package app.wffr.listeners;

import java.util.List;

import app.wffr.models.Product;

public interface ProductsListener {

    void OnLoading(boolean loading);
    void OnProductsGet(List<Product> products);
    void OnTotalBillProductsGet(List<Product> products);

}