package app.wffr.listeners;

import java.util.List;

import app.wffr.models.Product;

public interface FavoriteListener {

    void OnFavoritesGet(List<Product> productList);
    void OnAddRemoveFavorites(String response, Product product);
    void OnLoading(boolean loading);

}