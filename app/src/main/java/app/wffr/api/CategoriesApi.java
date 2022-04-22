package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.Category;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface CategoriesApi {

    // GET all categories
    @GET("ShopCategory")
    Call<List<Category>> getCategories(
            @HeaderMap Map<String, String> headers
    );

}