package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.CategoriesApi;
import app.wffr.models.Category;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesRepo {

    private static CategoriesApi categoriesApi;
    private MutableLiveData<List<Category>> categories;
    private static CategoriesRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static CategoriesRepo getInstance(int type){
        if (instance == null || type == Constants.REFRESH){
            instance = new CategoriesRepo();
        }
        return instance;
    }

    public CategoriesRepo(){
        isUpdating.setValue(true);
        categories = new MutableLiveData<List<Category>>();
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

        categoriesApi = retrofit.create(CategoriesApi.class);

        Call<List<Category>> call = categoriesApi.getCategories(
                AccessApi.getHeaders()
        );
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                categories.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<List<Category>> getCategories(){
        return categories;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}