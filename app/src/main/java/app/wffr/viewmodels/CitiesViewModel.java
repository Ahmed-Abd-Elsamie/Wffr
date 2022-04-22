package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.City;
import app.wffr.repositories.CitiesRepo;

public class CitiesViewModel extends ViewModel {

    private MutableLiveData<List<City>> cities;
    private CitiesRepo citiesRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
    }

    public void loadCities() {
        cities = new MutableLiveData<List<City>>();
        citiesRepo = CitiesRepo.getInstance();
        citiesRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        cities = citiesRepo.getCities();
    }

    public LiveData<List<City>> getCities() {
        return cities;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}
