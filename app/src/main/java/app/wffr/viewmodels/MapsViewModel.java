package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.MapsResponse;
import app.wffr.repositories.MapsRepo;

public class MapsViewModel extends ViewModel {

    private MutableLiveData<List<MapsResponse>> locations;
    private MapsRepo mapsRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
        locations = new MutableLiveData<List<MapsResponse>>();
    }

    public void loadAllLocations(String fromx, String tox, String fromy, String toy){
        mapsRepo = new MapsRepo();
        mapsRepo.getLocations(fromx, tox, fromy, toy);
        mapsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.postValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });
        locations = mapsRepo.getLocations();
    }


    public LiveData<List<MapsResponse>> getLocations() {
        return locations;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}