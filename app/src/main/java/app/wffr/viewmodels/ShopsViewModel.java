package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.listeners.ShopsListener;
import app.wffr.models.Shop;
import app.wffr.repositories.ShopsRepo;

public class ShopsViewModel extends ViewModel {
    private MutableLiveData<List<Shop>> shops;
    private MutableLiveData<String> rating;
    private ShopsRepo shopsRepo;
    private MutableLiveData<Boolean> isUpdatingFeat = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
    }

    public void loadShops(String categoryId, ShopsListener shopsListener, int page, int size) {
        shops = new MutableLiveData<>();
        shopsRepo = new ShopsRepo();
        shopsRepo.getShopsByCategory(categoryId, page, size);
        shopsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            shopsListener.OnLoadingData(aBoolean);
        });

        shopsRepo.getShops().observe(lifecycleOwner, shops -> {
            shopsListener.OnShopsGet(shops);
        });
    }

    public void ratShop(String rat, String clientId, String shopId) {
        rating = new MutableLiveData<String>();
        shopsRepo = new ShopsRepo();
        shopsRepo.ratShop(rat, clientId, shopId);
        shopsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdatingFeat.setValue(true);
            }else {
                isUpdatingFeat.postValue(false);
            }
        });

        rating = shopsRepo.getRatResponse();
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
    }

    public LiveData<String> getRating() {
        return rating;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdatingFeat;
    }

}