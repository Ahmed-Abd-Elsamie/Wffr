package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.SocialModel;
import app.wffr.repositories.SocialRepo;

public class SocialViewModel extends ViewModel {

    private MutableLiveData<List<SocialModel>> socialContact;
    private SocialRepo socialRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
        loadSocialContact();
    }

    public void loadSocialContact() {
        socialContact = new MutableLiveData<List<SocialModel>>();
        socialRepo = SocialRepo.getInstance();
        socialRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        socialContact = socialRepo.getSocialContact();
    }

    public LiveData<List<SocialModel>> getSocialContact() {
        return socialContact;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}
