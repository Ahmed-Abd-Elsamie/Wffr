package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import app.wffr.models.DashboardSettings;
import app.wffr.repositories.DashboardSettingsRepo;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<DashboardSettings> settings;
    private DashboardSettingsRepo settingsRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
        if (settings != null){
            return;
        }
        loadSettings();
    }

    private void loadSettings() {
        settings = new MutableLiveData<DashboardSettings>();
        settingsRepo = DashboardSettingsRepo.getInstance();
        settingsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        settings = settingsRepo.getSettings();
    }

    public LiveData<DashboardSettings> getDashboardSettings() {
        return settings;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}