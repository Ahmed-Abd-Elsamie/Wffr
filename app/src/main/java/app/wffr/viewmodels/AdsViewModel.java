package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import app.wffr.listeners.AdsListener;
import app.wffr.repositories.AdsRepo;

public class AdsViewModel extends ViewModel {

    private LifecycleOwner lifecycleOwner;
    private AdsRepo adsRepo;


    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    public void loadAds(int type, AdsListener adsListener) {

        adsRepo = AdsRepo.getInstance(type);
        adsRepo.getOffersAds();
        adsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            adsListener.OnLoadingAds(aBoolean);
        });

        adsRepo.getAds().observe(lifecycleOwner, adModels -> {
            adsListener.OnAdsGet(adModels);
        });

    }

    public void loadAdsCategory(int type, AdsListener adsListener, int categoryId) {
        adsRepo = AdsRepo.getInstance(type);
        adsRepo.getCategoryAds(categoryId);
        adsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            adsListener.OnLoadingAds(aBoolean);
        });

        adsRepo.getAdsCategory().observe(lifecycleOwner, adModels -> {
            adsListener.OnAdsCategoryGet(adModels);
        });

    }


}
