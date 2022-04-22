package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.FaqsApi;
import app.wffr.models.FAQModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FAQsRepo {
    private static FaqsApi faqsApi;
    private MutableLiveData<List<FAQModel>> faqs;
    private static FAQsRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static FAQsRepo getInstance(){
        if (instance == null){
            instance = new FAQsRepo();
        }
        return instance;
    }

    public FAQsRepo(){
        isUpdating.setValue(true);
        faqs = new MutableLiveData<List<FAQModel>>();
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

        faqsApi = retrofit.create(FaqsApi.class);

        Call<List<FAQModel>> call = faqsApi.getFAQs(AccessApi.getHeaders());
        call.enqueue(new Callback<List<FAQModel>>() {
            @Override
            public void onResponse(Call<List<FAQModel>> call, Response<List<FAQModel>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                faqs.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<FAQModel>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<List<FAQModel>> getFaqs(){
        return faqs;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}