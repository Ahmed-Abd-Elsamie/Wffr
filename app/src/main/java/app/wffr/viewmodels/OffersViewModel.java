package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.AdModel;
import app.wffr.models.Category;
import app.wffr.repositories.AdsRepo;
import app.wffr.repositories.CategoriesRepo;
import app.wffr.utils.Constants;

public class OffersViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<AdModel>> ads;
    private CategoriesRepo categoriesRepo;
    private AdsRepo adsRepo;
    private MutableLiveData<Boolean> isUpdatingCategories = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdatingAds = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;


    public void init(Activity context, int type){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;

        if (categories == null || type == Constants.REFRESH) {
            categories = new MutableLiveData<List<Category>>();
            loadCategories(type);
        }

        if (ads == null || type == Constants.REFRESH) {
            ads = new MutableLiveData<List<AdModel>>();
            loadAds(type);
        }

    }

    private void loadCategories(int type) {
        categoriesRepo = CategoriesRepo.getInstance(type);
        categoriesRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdatingCategories.setValue(true);
                }else {
                    isUpdatingCategories.postValue(false);
                }
            }
        });
        categories = categoriesRepo.getCategories();
    }

    private void loadAds(int type) {
        adsRepo = AdsRepo.getInstance(type);
        adsRepo.getOffersAds();
        adsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdatingAds.setValue(true);
                }else {
                    isUpdatingAds.postValue(false);
                }
            }
        });
        ads = adsRepo.getAds();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<AdModel>> getAds() {
        return ads;
    }

    public LiveData<Boolean> getIsUpdatingCategories(){
        return isUpdatingCategories;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }
}