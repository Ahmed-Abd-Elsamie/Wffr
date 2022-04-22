package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.AccountApi;
import app.wffr.models.CategoryRatio;
import app.wffr.models.UserAccount;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAccountRepo {
    private static AccountApi accountApi;
    private MutableLiveData<UserAccount> userAccount;
    private MutableLiveData<String> pointsResponse;
    private MutableLiveData<List<CategoryRatio>> categories;
    private static UserAccountRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static UserAccountRepo getInstance(){
        instance = new UserAccountRepo();
        return instance;
    }

    public void getCategoriesRatios(String clientId){
        isUpdating.setValue(true);
        categories = new MutableLiveData<List<CategoryRatio>>();
        Call<List<CategoryRatio>> call = accountApi.getCategoriesRatio(AccessApi.getHeaders(), clientId);
        call.enqueue(new Callback<List<CategoryRatio>>() {
            @Override
            public void onResponse(Call<List<CategoryRatio>> call, Response<List<CategoryRatio>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                categories.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<CategoryRatio>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public void getMoneyData(String clientId){
        isUpdating.setValue(true);
        userAccount = new MutableLiveData<UserAccount>();
        Call<UserAccount> call = accountApi.getUserAccountData(AccessApi.getHeaders(), clientId);
        call.enqueue(new Callback<UserAccount>() {
            @Override
            public void onResponse(Call<UserAccount> call, Response<UserAccount> response) {
                if (!response.isSuccessful()){
                    return;
                }
                userAccount.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<UserAccount> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public void exchangePoints(String clientId){
        isUpdating.setValue(true);
        pointsResponse = new MutableLiveData<>();
        Call<String> call = accountApi.exchangePoints(AccessApi.getHeaders(), clientId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    return;
                }
                pointsResponse.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public UserAccountRepo(){
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

        accountApi = retrofit.create(AccountApi.class);
    }

    public MutableLiveData<UserAccount> getUserAccount(){
        return userAccount;
    }


    public MutableLiveData<String> getPointsResponse(){
        return pointsResponse;
    }

    public MutableLiveData<List<CategoryRatio>> getUserCategoriesRatio(){
        return categories;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}
