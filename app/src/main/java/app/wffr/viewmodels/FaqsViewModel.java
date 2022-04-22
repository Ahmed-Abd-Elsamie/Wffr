package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.FAQModel;
import app.wffr.repositories.FAQsRepo;

public class FaqsViewModel extends ViewModel {

    private MutableLiveData<List<FAQModel>> faqs;
    private FAQsRepo faQsRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;

        if (faqs == null) {
            faqs = new MutableLiveData<>();
            loadFaqs();
        }

    }

    private void loadFaqs() {
        faQsRepo = FAQsRepo.getInstance();
        faQsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });
        faqs = faQsRepo.getFaqs();
    }

    public LiveData<List<FAQModel>> getFaqs() {
        return faqs;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}