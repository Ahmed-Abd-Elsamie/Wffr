package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.NotificationItem;
import app.wffr.models.Product;
import app.wffr.repositories.NotificationsRepo;

public class NotificationsViewModel extends ViewModel {
    private MutableLiveData<List<NotificationItem>> notifications;
    private MutableLiveData<List<Product>> productNotifications;
    private NotificationsRepo notificationsRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
    }

    public void loadNotifications(String clientId) {
        notifications = new MutableLiveData<List<NotificationItem>>();
        notificationsRepo = new NotificationsRepo();
        notificationsRepo.getNotifications(clientId);
        notificationsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });
        notifications = notificationsRepo.getNotifications();
    }

    public void loadProductsNotifications(String date) {
        productNotifications = new MutableLiveData<List<Product>>();
        notificationsRepo = new NotificationsRepo();
        notificationsRepo.getProductsNotifications(date);
        notificationsRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        notificationsRepo.getProductNotifications().observe(lifecycleOwner, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productNotifications.postValue(products);
            }
        });

    }



    public LiveData<List<NotificationItem>> getNotifications() {
        return notifications;
    }

    public LiveData<List<Product>> getProductsNotifications() {
        return productNotifications;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}