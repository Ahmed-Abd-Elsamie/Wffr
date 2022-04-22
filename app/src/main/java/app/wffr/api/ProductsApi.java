package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.FavoriteRequest;
import app.wffr.models.FeaturedOffersResponse;
import app.wffr.models.Product;
import app.wffr.models.ProductPreviewResponse;
import app.wffr.models.SearchModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductsApi {

    // GET all featured products
    @GET("ProductSpecial3")
    Call<FeaturedOffersResponse> getFeaturedProducts(
            @HeaderMap Map<String, String> headers,
            @Query("ClientID") String clientId
    );

    // GET weekly offers
    @GET("Products?ShowType=false&Type=false")
    Call<List<Product>> getWeeklyOffers(
            @HeaderMap Map<String, String> headers
    );

    // GET weekly offers
    @GET("Products?ShowType=false&Type=false")
    Call<List<Product>> getWeeklyOffers(
            @HeaderMap Map<String, String> headers,
            @Query("Category") String category
    );

    // GET totalBill
    @GET("Products?ShowType=false&Type=true")
    Call<List<Product>> getTotalBill(
            @HeaderMap Map<String, String> headers
    );


    // GET favorites
    @GET("Favorites")
    Call<List<Product>> getFavorites(
            @HeaderMap Map<String, String> headers,
            @Query("client") String client,
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    // GET favorites
    @GET("Favorites")
    Call<String> getIsFavorite(
            @HeaderMap Map<String, String> headers,
            @Query("ClientID") String clientId,
            @Query("ProductID") String productId
    );

    @POST("Favorites")
    Call<String> addRemoveFavorites(
            @HeaderMap Map<String, String> headers,
            @Body FavoriteRequest favoriteRequest
    );

    // GET products
    @GET("Products")
    Call<List<Product>> getProducts(
            @HeaderMap Map<String, String> headers,
            @Query("Category") String category,
            @Query("show") boolean showType,
            @Query("type") boolean type,
            @Query("_from") String from,
            @Query("to") String to
    );

    // GET products
    @GET("products/search")
    Call<SearchModel> getGeneralProducts(
            @HeaderMap Map<String, String> headers,
            @Query("category") String category,
            @Query("from") String from,
            @Query("to") String to,
            @Query("key") String key,
            @Query("page_total") Integer page_total_bill,
            @Query("size") Integer size,
            @Query("page_products") Integer page_product
    );

    @GET("Products")
    Call<SearchModel> getShopProducts(
            @HeaderMap Map<String, String> headers,
            @Query("shopID") String shopId
    );

    @GET("Products")
    Call<SearchModel> getProductsByShopName(
            @HeaderMap Map<String, String> headers,
            @Query("ShopName") String shopName
    );

    @GET("Products?=false&=false")
    Call<List<Product>> getProductsOfShop(
            @HeaderMap Map<String, String> headers,
            @Query("shopID") String category,
            @Query("show") boolean showType,
            @Query("type") boolean type
    );

    @GET("Shop")
    Call<ProductPreviewResponse> getProductData(
            @HeaderMap Map<String, String> headers,
            @Query("productID") String productId,
            @Query("clientID") String clientId
    );


}