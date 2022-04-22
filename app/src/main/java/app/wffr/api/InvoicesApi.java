package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.Invoice;
import app.wffr.models.MonthReportResponse;
import app.wffr.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface InvoicesApi {

    @GET("ShopInvois")
    Call<List<Invoice>> getPrevOrders(
            @HeaderMap Map<String, String> headers,
            @Query("ClientID") String clientId
    );

    @GET("ShopInvois")
    Call<List<Product>> getPrevOrdersProducts(
            @HeaderMap Map<String, String> headers,
            @Query("client") String clientId
    );

    @GET("ShopInvois/GetReport")
    Call<MonthReportResponse> getPrevOrdersByMonth(
            @HeaderMap Map<String, String> headers,
            @Query("shop") String shopId,
            @Query("client") String clientId,
            @Query("from") String from,
            @Query("to") String to
    );



}