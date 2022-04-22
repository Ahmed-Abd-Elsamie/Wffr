package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.listeners.ProductsListener;
import app.wffr.models.Product;
import app.wffr.models.ProductPreviewResponse;
import app.wffr.models.SearchModel;
import app.wffr.repositories.GeneralProductsRepo;

public class GeneralProductViewModel extends ViewModel {

    private MutableLiveData<List<Product>> featuredProducts;
    private MutableLiveData<ProductPreviewResponse> productData;
    private MutableLiveData<List<Product>> totalBillProducts;
    private GeneralProductsRepo generalProductsRepo;
    private GeneralProductsRepo generalSearchProductsRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;

        if (featuredProducts == null) {
            featuredProducts = new MutableLiveData<List<Product>>();
        }

        if (totalBillProducts == null) {
            totalBillProducts = new MutableLiveData<List<Product>>();
        }

        if (productData == null) {
            productData = new MutableLiveData<>();
        }

    }

    public void loadAllProducts(String category, String from, String to, String shopName, int type, int page, int size, Integer pageProduct){
        generalProductsRepo = GeneralProductsRepo.getInstance(type);
        generalProductsRepo.getGeneralProducts(category, from, to, shopName, page, size, pageProduct);
        generalProductsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.postValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        generalProductsRepo.getGeneralProducts().observe(lifecycleOwner, new Observer<SearchModel>() {
            @Override
            public void onChanged(SearchModel products) {
                if (products == null){
                    totalBillProducts.postValue(null);
                    featuredProducts.postValue(null);
                    return;
                }
                featuredProducts.postValue(products.getProducts());
                totalBillProducts.postValue(products.getTotalProducts());
            }
        });
    }

    public void loadGeneralProducts(String category, String from, String to, String shopName, int type, ProductsListener productsListener, Integer page, Integer size, Integer pageProduct){
        generalProductsRepo = GeneralProductsRepo.getInstance(type);
        generalProductsRepo.getGeneralProducts(category, from, to, shopName, page, size, pageProduct);
        generalProductsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                /*if (aBoolean){
                    isUpdating.postValue(true);
                }else {
                    isUpdating.postValue(false);
                }*/
                productsListener.OnLoading(aBoolean);
            }
        });

        generalProductsRepo.getGeneralProducts().observe(lifecycleOwner, products -> {
            if (products == null){
                //totalBillProducts.postValue(null);
                //featuredProducts.postValue(null);
                productsListener.OnProductsGet(null);
                productsListener.OnTotalBillProductsGet(null);
                return;
            }
            productsListener.OnProductsGet(products.getProducts());
            productsListener.OnTotalBillProductsGet(products.getTotalProducts());
            //featuredProducts.postValue(products.getProducts());
            //totalBillProducts.postValue(products.getTotalProducts());
        });
    }

    public void searchGeneralProducts(String category, String from, String to, String shopName, int type, ProductsListener productsListener, Integer page, Integer size, Integer pageProduct){
        generalSearchProductsRepo = new GeneralProductsRepo();
        generalSearchProductsRepo.getGeneralProducts(category, from, to, shopName, page, size, pageProduct);
        generalSearchProductsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                productsListener.OnLoading(aBoolean);
            }
        });

        generalSearchProductsRepo.getGeneralProducts().observe(lifecycleOwner, products -> {
            if (products == null){
                //totalBillProducts.postValue(null);
                //featuredProducts.postValue(null);
                productsListener.OnProductsGet(null);
                productsListener.OnTotalBillProductsGet(null);
                return;
            }
            productsListener.OnProductsGet(products.getProducts());
            productsListener.OnTotalBillProductsGet(products.getTotalProducts());
            //featuredProducts.postValue(products.getProducts());
            //totalBillProducts.postValue(products.getTotalProducts());
        });
    }

    public void loadProductData(String productId, String clientId){
        generalProductsRepo = new GeneralProductsRepo();
        generalProductsRepo.getProductData(productId, clientId);
        generalProductsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.postValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        generalProductsRepo.getProductData().observe(lifecycleOwner, new Observer<ProductPreviewResponse>() {
            @Override
            public void onChanged(ProductPreviewResponse product) {
                if (product == null){
                    productData.postValue(null);
                    return;
                }
                productData.postValue(product);
            }
        });

    }

    public void loadShopProducts(String shopId){
        generalProductsRepo = new GeneralProductsRepo();
        generalProductsRepo.getShopProducts(shopId);
        generalProductsRepo.getIsUpdating().removeObservers(lifecycleOwner);
        generalProductsRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isUpdating.postValue(true);
                }else {
                    isUpdating.postValue(false);
                }
            }
        });

        generalProductsRepo.getGeneralProducts().removeObservers(lifecycleOwner);
        generalProductsRepo.getGeneralProducts().observe(lifecycleOwner, new Observer<SearchModel>() {
            @Override
            public void onChanged(SearchModel products) {
                if (products == null){
                    totalBillProducts.postValue(null);
                    featuredProducts.postValue(null);
                    return;
                }
                featuredProducts.postValue(products.getProducts());
                totalBillProducts.postValue(products.getTotalProducts());
            }
        });
    }

    public LiveData<List<Product>> getFeaturedProducts() {
        return featuredProducts;
    }

    public LiveData<ProductPreviewResponse> getProductData() {
        return productData;
    }

    public LiveData<List<Product>> getTotalBillProducts() {
        return totalBillProducts;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}