package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;


import app.wffr.listeners.FeaturedProductListener;
import app.wffr.repositories.FeaturedOffersRepo;

public class FeaturedViewModel extends ViewModel {

    private FeaturedOffersRepo featuredOffersRepo;
    private LifecycleOwner lifecycleOwner;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    public void loadFeatured(String clientId, FeaturedProductListener featuredProductListener, int type) {
        featuredOffersRepo = FeaturedOffersRepo.getInstance(clientId, type);
        featuredOffersRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                featuredProductListener.OnLoadingData(aBoolean);
            }
        });

        featuredOffersRepo.getFeaturedOffers().observe(lifecycleOwner, featuredOffersResponse -> {
            if (featuredOffersResponse == null){
                featuredProductListener.OnProductsGet(null, null);
            }else {
                featuredProductListener.OnProductsGet(featuredOffersResponse.getProducts(), featuredOffersResponse.getSlider());
            }
        });
    }


}