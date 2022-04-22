package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import app.wffr.models.CouponModel;
import app.wffr.repositories.CouponRepo;

public class CouponViewModel extends ViewModel {

    private MutableLiveData<CouponModel> coupon;
    private CouponRepo couponRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
    }

    public void requestNewCoupon(String productId, String clientId) {
        coupon = new MutableLiveData<CouponModel>();
        couponRepo = new CouponRepo();
        couponRepo.requestCoupon(productId, clientId);
        couponRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        coupon = couponRepo.getCoupon();
    }

    public LiveData<CouponModel> getCoupon() {
        return coupon;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}