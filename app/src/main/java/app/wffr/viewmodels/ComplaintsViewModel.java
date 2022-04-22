package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.wffr.listeners.UserComplaintsListener;
import app.wffr.models.Complaint;
import app.wffr.models.FeedbackModel;
import app.wffr.repositories.ComplaintsRepo;


public class ComplaintsViewModel extends ViewModel {

    private ComplaintsRepo complaintsRepo;
    private MutableLiveData<String> message;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private MutableLiveData<String> feedResponse;

    public void init(UserComplaintsListener userComplaintsListener){
        complaintsRepo = new ComplaintsRepo(userComplaintsListener);
    }

    public void insertComplaint(Complaint complaint, String clientId){
        complaintsRepo.insertComplaint(complaint, clientId);
    }

    public void sendFeedback(Activity context, FeedbackModel feedbackModel) {
        this.lifecycleOwner = (LifecycleOwner) context;
        feedResponse = new MutableLiveData<String>();
        complaintsRepo = new ComplaintsRepo();
        complaintsRepo.sendFeedback(feedbackModel);
        complaintsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        feedResponse = complaintsRepo.getFeedResponse();
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

    public LiveData<String> getFeedResponse() {
        return feedResponse;
    }


}