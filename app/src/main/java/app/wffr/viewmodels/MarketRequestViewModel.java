package app.wffr.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.wffr.listeners.MarketRequestListener;
import app.wffr.models.MarketRequest;
import app.wffr.repositories.MarketRequestRepo;

public class MarketRequestViewModel extends ViewModel {

    private MarketRequestRepo marketRequestRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();


    public void init(MarketRequestListener marketRequestListener){
        marketRequestRepo = new MarketRequestRepo(marketRequestListener);
    }

    public void addMarket(MarketRequest marketRequest){
        marketRequestRepo.insertMarket(marketRequest);
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}