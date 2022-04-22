package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.models.Invoice;
import app.wffr.models.MonthInvoice;
import app.wffr.models.MonthReportResponse;
import app.wffr.models.Product;
import app.wffr.models.ShopInvoiceModel;
import app.wffr.repositories.PrevOrdersRepo;

public class PrevOrdersViewModel extends ViewModel {

    private MutableLiveData<List<Invoice>> orders;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<List<MonthInvoice>> monthInvoices;
    private MutableLiveData<List<ShopInvoiceModel>> shops;
    private PrevOrdersRepo prevOrdersRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
        if (orders == null) {
            orders = new MutableLiveData<List<Invoice>>();
        }

        if (products == null) {
            products = new MutableLiveData<List<Product>>();
        }

        if (monthInvoices == null) {
            monthInvoices = new MutableLiveData<List<MonthInvoice>>();
        }

        if (shops == null) {
            shops = new MutableLiveData<List<ShopInvoiceModel>>();
        }
    }

    public void loadPrevOrders(String clientId) {
        prevOrdersRepo = new PrevOrdersRepo();
        prevOrdersRepo.getPrevOrders(clientId);
        prevOrdersRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        orders = prevOrdersRepo.getPevOrders();
    }

    public void loadPrevOrdersProducts(String clientId) {
        prevOrdersRepo = new PrevOrdersRepo();
        prevOrdersRepo.getPrevOrdersProducts(clientId);
        prevOrdersRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        products = prevOrdersRepo.getPevOrdersProducts();
    }

    public void loadPrevOrdersByMonth(String shopId, String clientId, String from, String to) {
        prevOrdersRepo = new PrevOrdersRepo();
        prevOrdersRepo.getPrevOrdersByMonth(shopId, clientId, from, to);
        prevOrdersRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.setValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        prevOrdersRepo.getPevOrdersByMonth().observe(lifecycleOwner, new Observer<MonthReportResponse>() {
            @Override
            public void onChanged(MonthReportResponse reportResponse) {
                if (reportResponse == null){
                    monthInvoices.postValue(null);
                    shops.postValue(null);
                    return;
                }
                monthInvoices.postValue(reportResponse.getMonthInvoices());
                shops.postValue(reportResponse.getShopInvoiceModels());
            }
        });


    }

    public LiveData<List<Invoice>> getPrevOrders() {
        return orders;
    }

    public LiveData<List<Product>> getPrevOrdersProducts() {
        return products;
    }

    public LiveData<List<MonthInvoice>> getMonthReports() {
        return monthInvoices;
    }

    public LiveData<List<ShopInvoiceModel>> getPrevShops() {
        return shops;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}
