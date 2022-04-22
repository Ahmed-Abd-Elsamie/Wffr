package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.SocialApi;
import app.wffr.models.SocialModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SocialRepo {

    private static SocialApi socialApi;
    private MutableLiveData<List<SocialModel>> socialContact;
    private static SocialRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static SocialRepo getInstance(){
        if (instance == null){
            instance = new SocialRepo();
        }
        return instance;
    }

    public SocialRepo(){
        isUpdating.setValue(true);
        socialContact = new MutableLiveData<List<SocialModel>>();
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

        socialApi = retrofit.create(SocialApi.class);

        Call<List<SocialModel>> call = socialApi.getSocialContact(AccessApi.getHeaders());
        call.enqueue(new Callback<List<SocialModel>>() {
            @Override
            public void onResponse(Call<List<SocialModel>> call, Response<List<SocialModel>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                socialContact.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<SocialModel>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<List<SocialModel>> getSocialContact(){
        return socialContact;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}