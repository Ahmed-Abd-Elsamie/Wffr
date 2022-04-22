package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.CouponApi;
import app.wffr.models.CouponModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CouponRepo {

    private static CouponApi couponApi;
    private MutableLiveData<CouponModel> coupon;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public CouponRepo(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        couponApi = retrofit.create(CouponApi.class);
    }

    public void getNewCoupon(){
        /*coupon = new MutableLiveData<String>();
        isUpdating.setValue(true);
        Call<String> call = couponApi.getCoupon();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    return;
                }
                coupon.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });*/

    }

    public void checkPreCoupon(String clientId, String shopId, String productId){
        /*coupon = new MutableLiveData<String>();
        isUpdating.setValue(true);
        Call<String> call = couponApi.checkPrevCoupon(clientId, shopId, productId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    return;
                }
                coupon.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });*/

    }

    public void requestCoupon(String productId, String clientId){
        coupon = new MutableLiveData<CouponModel>();
        isUpdating.setValue(true);
        Call<CouponModel> call = couponApi.requestCoupon(AccessApi.getHeaders(), productId, clientId);
        call.enqueue(new Callback<CouponModel>() {
            @Override
            public void onResponse(Call<CouponModel> call, Response<CouponModel> response) {
                if (!response.isSuccessful()){
                    return;
                }
                coupon.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<CouponModel> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }


    public MutableLiveData<CouponModel> getCoupon(){
        return coupon;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}