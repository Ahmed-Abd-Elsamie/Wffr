package app.wffr.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.wffr.R;
import app.wffr.WffrBaseFragment;
import app.wffr.adapters.CategoriesRadioAdapter;
import app.wffr.adapters.ProductsAdapter;
import app.wffr.adapters.ProductsAdapterFill;
import app.wffr.databinding.SearchFragmentBinding;
import app.wffr.listeners.CategoriesListener;
import app.wffr.listeners.ProductsListener;
import app.wffr.models.Category;
import app.wffr.models.Product;
import app.wffr.ui.activities.MapsActivity;
import app.wffr.utils.Constants;
import app.wffr.utils.utils;
import app.wffr.viewmodels.CategoriesViewModel;
import app.wffr.viewmodels.GeneralProductViewModel;

public class SearchFragment extends WffrBaseFragment implements ProductsListener, CategoriesListener {

    private SearchFragmentBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private AppCompatActivity context;
    private ProductsAdapter featuredAdapter;
    private ProductsAdapterFill totalBillAdapter;
    private CategoriesRadioAdapter categoriesRadioAdapter;
    public static RangeSlider rangeSlider;
    private GeneralProductViewModel generalProductViewModel;
    public static String categoryName = "0";
    public static String shopName = "";
    private Set<String> autoSearchSet = new HashSet<>();
    private List<String> autoSearchList = new ArrayList<>();
    public static int total_bill_page = 0;
    public static int products_page = 0;
    private int size = 10;
    private boolean isLoadingTotalBill = false;
    private boolean isLoadingFeatured = false;
    private List<Product> totalBillProducts;
    private List<Product> featuredProducts;
    public static boolean categoryChangeFeat = false;
    public static boolean categoryChangeTotal = false;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
        context = (AppCompatActivity) getActivity();

        totalBillProducts = new ArrayList<>();
        featuredProducts = new ArrayList<>();

        // reset counter
        total_bill_page = 0;
        products_page = 0;

        initRecyclerView();

        // initialize view models
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        generalProductViewModel = new ViewModelProvider(this).get(GeneralProductViewModel.class);
        generalProductViewModel.init(context);
        categoriesViewModel.init(context);

        // generalize range bar
        rangeSlider = binding.rangePrice;

        // get all data
        generalProductViewModel.searchGeneralProducts("0", "0", "100", "",  Constants.REFRESH, this, total_bill_page, size, products_page);

        // load categories
        categoriesViewModel.loadCategories(Constants.LOAD, this);

        // listen for range bar changing
        binding.rangePrice.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                categoryChangeFeat = true;
                categoryChangeTotal = true;
                total_bill_page = 0;
                products_page = 0;
                searchProducts();
            }
        });

        // searching by name change
        binding.txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!TextUtils.isEmpty(binding.txtSearch.getText().toString())){
                    categoryChangeFeat = true;
                    categoryChangeTotal = true;
                    total_bill_page = 0;
                    products_page = 0;
                    searchProducts();
                }
                binding.txtSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(binding.txtSearch.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        binding.btnLocation.setOnClickListener(v -> {

            // check GPS open state
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
                return;
            }

            // check for location permission
            if (!utils.checkLocationPermissions(context)) {
                utils.verifyLocationPermissions(context);
                return;
            }

            startActivity(new Intent(context, MapsActivity.class));
        });

        binding.pullToRefresh.setOnRefreshListener(() -> {
            // reset data
            categoryName = "0";
            shopName = "";
            categoryChangeFeat = true;
            categoryChangeTotal = true;
            binding.txtSearch.setText("");
            List<Float> vals = new ArrayList<>();
            vals.add((float) 0);
            vals.add((float) 100);
            binding.rangePrice.setValues(vals);
            binding.txtFrom.setText("0");
            binding.txtTo.setText("100");

            total_bill_page = 0;
            products_page = 0;

            // refresh products
            searchProducts();
            // refresh categories
            categoriesViewModel.loadCategories(Constants.REFRESH, SearchFragment.this);
        });

        // updating shop name values
        binding.txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopName = binding.txtSearch.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.recyclerAll.getLayoutManager();

                System.out.println("SSSS : " + (totalBillProducts.size()-1) + " : lay : " + linearLayoutManager.findLastCompletelyVisibleItemPosition());

                if (!isLoadingTotalBill) {
                    if (totalBillProducts.size() >= size && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalBillProducts.size() - 1) {
                        //bottom of list!
                        total_bill_page++;
                        isLoadingTotalBill = true;
                        searchProducts();
                    }
                }

            }
        });

        binding.recyclerFeatured.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                System.out.println("SSSS : " + (featuredProducts.size()-1) + " : lay : " + linearLayoutManager.findLastCompletelyVisibleItemPosition());

                if (!isLoadingFeatured) {
                    if (featuredProducts.size() >= size && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == featuredProducts.size() - 1) {
                        //bottom of list!
                        products_page++;
                        isLoadingFeatured = true;
                        searchProducts();
                    }
                }
            }
        });

        View view = binding.getRoot();
        return view;
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager allLayoutManager = new GridLayoutManager(context, Integer.parseInt(getString(R.string.column_count)), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        binding.recyclerFeatured.setLayoutManager(featuredLayoutManager);
        binding.recyclerAll.setLayoutManager(allLayoutManager);
        binding.recyclerCategory.setLayoutManager(categoryLayoutManager);
    }


    @Override
    public void OnLoadingCategories(boolean loading) {
        binding.pullToRefresh.setRefreshing(loading);
    }

    @Override
    public void OnCategoriesGet(List<Category> categories) {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(
                "all",
                "كل العروض",
                "All Offers",
                "true",
                "",
                true
        ));

        categoryList.addAll(categories);

        categoriesRadioAdapter = new CategoriesRadioAdapter(context, categoryList, generalProductViewModel, this);
        binding.recyclerCategory.setAdapter(categoriesRadioAdapter);
        categoriesRadioAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnLoading(boolean loading) {
        if (loading){
            showProgressBar(binding.progressBarFeat);
        }else {
            hideProgressBar(binding.progressBarFeat);
        }
    }

    @Override
    public void OnProductsGet(List<Product> products) {

        if (categoryChangeFeat){
            featuredProducts.clear();
            products_page = 0;
            binding.recyclerFeatured.setAdapter(null);
            categoryChangeFeat = false;
        }

        if ((products == null || products.size() == 0) && featuredProducts.size() == 0){
            /*binding.imgNoFeat.setVisibility(View.VISIBLE);
            binding.txtFeat.setText(R.string.no_featured_offers);
            binding.recyclerFeatured.setAdapter(null);*/
            binding.imgNoFeat.setVisibility(View.GONE);
            binding.txtFeat.setVisibility(View.GONE);
            binding.recyclerFeatured.setAdapter(null);
            return;
        }

        int oldS = featuredProducts.size();

        if (oldS == 0){
            featuredProducts.addAll(products);
        }else {
            if (oldS >= size){

                if (featuredProducts.get(featuredProducts.size()-1) == null){
                    featuredProducts.remove(featuredProducts.size() - 1);

                    featuredAdapter.notifyItemRemoved(featuredProducts.size());
                    isLoadingFeatured = false;
                }

                if (products.size() > 0){
                    if (!(products.get(products.size() - 1).get_id().equals(featuredProducts.get(featuredProducts.size() - 1).get_id()))){
                        featuredProducts.addAll(products);
                        featuredAdapter.notifyItemRangeChanged(oldS, featuredProducts.size() - 1);
                    }
                }
            }
        }

        if (featuredAdapter == null || binding.recyclerFeatured.getAdapter() == null){
            featuredAdapter = new ProductsAdapter(context, featuredProducts, "");
            binding.recyclerFeatured.setAdapter(featuredAdapter);
        }
        binding.imgNoFeat.setVisibility(View.VISIBLE);
        binding.txtFeat.setVisibility(View.VISIBLE);
        binding.imgNoFeat.setVisibility(View.GONE);
        binding.progressBarFeat.setVisibility(View.GONE);
    }

    @Override
    public void OnTotalBillProductsGet(List<Product> products) {

        if (categoryChangeTotal){
            totalBillProducts.clear();
            total_bill_page = 0;
            binding.recyclerAll.setAdapter(null);
            categoryChangeTotal= false;
        }

        if ((products == null || products.size() == 0) && totalBillProducts.size() == 0){
            /*binding.imgNoTotal.setVisibility(View.VISIBLE);
            binding.txtTotalBill.setText(R.string.no_total_bill);
            binding.recyclerAll.setAdapter(null);*/
            if (totalBillProducts.size() == 0 && featuredProducts.size() == 0){
                binding.imgNoTotal.setVisibility(View.VISIBLE);
                binding.txtTotalBill.setText(R.string.no_products);
                binding.recyclerAll.setAdapter(null);
            }else {
                binding.imgNoTotal.setVisibility(View.GONE);
                binding.txtTotalBill.setVisibility(View.GONE);
                binding.recyclerAll.setAdapter(null);
            }
            return;
        }

        int oldS = totalBillProducts.size();

        if (oldS == 0){
            totalBillProducts.addAll(products);
        }else {
            if (oldS >= size){

                if (totalBillProducts.get(totalBillProducts.size()-1) == null){
                    totalBillProducts.remove(totalBillProducts.size() - 1);

                    totalBillAdapter.notifyItemRemoved(totalBillProducts.size());
                    isLoadingTotalBill = false;
                }

                if (products.size() > 0){
                    if (!(products.get(products.size() - 1).get_id().equals(totalBillProducts.get(totalBillProducts.size() - 1).get_id()))){
                        totalBillProducts.addAll(products);
                        totalBillAdapter.notifyItemRangeChanged(oldS, totalBillProducts.size() - 1);
                    }
                }
            }
        }

        if (totalBillAdapter == null || binding.recyclerAll.getAdapter() == null){
            totalBillAdapter = new ProductsAdapterFill(context, totalBillProducts, "");
            binding.recyclerAll.setAdapter(totalBillAdapter);
        }

        binding.imgNoTotal.setVisibility(View.VISIBLE);
        binding.txtTotalBill.setVisibility(View.VISIBLE);
        binding.imgNoTotal.setVisibility(View.GONE);
        binding.progressBarFeat.setVisibility(View.GONE);
        binding.txtTotalBill.setText(R.string.total_bill);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Open Gps")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void searchProducts(){
        int fromRange = Math.round(binding.rangePrice.getValues().get(0));
        int toRange = Math.round(binding.rangePrice.getValues().get(1));
        binding.txtFrom.setText(fromRange + "");
        binding.txtTo.setText(toRange + "");
        shopName = binding.txtSearch.getText().toString();

        if (isLoadingTotalBill){
            totalBillProducts.add(null);
            totalBillAdapter.notifyItemInserted(totalBillProducts.size() - 1);
        }

        if (isLoadingFeatured){
            featuredProducts.add(null);
            featuredAdapter.notifyItemInserted(featuredProducts.size() - 1);
        }

        // load products
        generalProductViewModel.searchGeneralProducts(categoryName, fromRange+"", toRange+"", shopName,  Constants.REFRESH, this, total_bill_page, size, products_page);
    }


}