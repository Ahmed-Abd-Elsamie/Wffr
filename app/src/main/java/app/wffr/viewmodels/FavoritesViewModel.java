package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.listeners.FavoriteListener;
import app.wffr.models.FavoriteRequest;
import app.wffr.models.Product;
import app.wffr.repositories.FavoritesRepo;

public class FavoritesViewModel extends ViewModel {

    private FavoritesRepo favoritesRepo;
    private LifecycleOwner lifecycleOwner;
    private FavoriteListener favoriteListener;

    public void init(Activity context, FavoriteListener favoriteListener){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.favoriteListener = favoriteListener;
        favoritesRepo = new FavoritesRepo();
    }

    public void loadFavorites(String clientId, int page, Integer size) {
        favoritesRepo.getFavProducts(clientId, page, size);
        favoritesRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            favoriteListener.OnLoading(aBoolean);
        });

        favoritesRepo.getProducts().observe(lifecycleOwner, productsList -> {
            favoriteListener.OnFavoritesGet(productsList);
        });

    }

    public void addRemoveFavorites(FavoriteRequest favoriteRequest, Product product){
        favoritesRepo.addToFavProducts(favoriteRequest);
        favoritesRepo.getAddRemoveResponse().observe(lifecycleOwner, s -> {
            favoriteListener.OnAddRemoveFavorites(s, product);
        });
    }

}
