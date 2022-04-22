package app.wffr.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.AdsCategorySliderAdapter;
import app.wffr.adapters.ProductsAdapter;
import app.wffr.adapters.ShopsAdapter;
import app.wffr.databinding.ActivityCategoriesViewBinding;
import app.wffr.listeners.AdsListener;
import app.wffr.listeners.ShopsListener;
import app.wffr.models.AdCategoryModel;
import app.wffr.models.AdModel;
import app.wffr.models.Category;
import app.wffr.models.Shop;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.Constants;
import app.wffr.viewmodels.AdsViewModel;
import app.wffr.viewmodels.ShopsViewModel;

public class CategoriesViewActivity extends WffrBaseActivity implements ShopsListener, AdsListener {

    private ActivityCategoriesViewBinding binding;
    private ShopsViewModel shopsViewModel;
    private AdsViewModel adsViewModel;
    private Category category;
    private ShopsAdapter adapter;
    private AdsCategorySliderAdapter adsSliderAdapter;
    private int page = 0;
    private List<Shop> allShopsList;
    private boolean isLoading = false;
    private int size = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categories_view);

        allShopsList = new ArrayList<>();

        // initialize recycler view
        initRecyclerView();

        // get passed category
        Intent intent = getIntent();
        category = intent.getParcelableExtra("category");

        // check language
        if(LocalRepo.getLanguage(CategoriesViewActivity.this).equals("en")){
            binding.txtCategoryTitle.setText(category.getName_en());
        }else {
            binding.txtCategoryTitle.setText(category.getName());
        }

        //Glide.with(this).load("https://wffr.app" + category.getImg()).into(binding.imgCategory);

        // initialize view models
        shopsViewModel = new ViewModelProvider(this).get(ShopsViewModel.class);
        shopsViewModel.init(this);
        adsViewModel = new ViewModelProvider(this).get(AdsViewModel.class);
        adsViewModel.init(this);

        // load ads
        adsViewModel.loadAdsCategory(Constants.REFRESH, this, Integer.parseInt(category.getId()));
        // load shops
        shopsViewModel.loadShops(category.getId(), this, page, size);

        // handle slider
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();


        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.recyclerShops.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (allShopsList.size() >= size && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == allShopsList.size() - 1) {
                        //bottom of list!
                        page++;
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });



    }

    private void initRecyclerView() {
        LinearLayoutManager featuredLayoutManager = new GridLayoutManager(this, Integer.parseInt(getString(R.string.column_count)), LinearLayoutManager.VERTICAL, false);
        binding.recyclerShops.setLayoutManager(featuredLayoutManager);
        // set animations
        LayoutAnimationController featuredAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerShops.setLayoutAnimation(featuredAnim);
        binding.recyclerShops.scheduleLayoutAnimation();
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void OnLoadingData(boolean loading) {
        if (loading){
            showProgressBar(binding.progressBarShops);
        }else {
            hideProgressBar(binding.progressBarShops);
        }
    }

    @Override
    public void OnShopsGet(List<Shop> shops) {
        /*if (shops.size() == 0 || shops == null){
            binding.recyclerShops.setAdapter(null);
            binding.imgNoData.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.VISIBLE);
            return;
        }

        if(LocalRepo.getLanguage(CategoriesViewActivity.this).equals("en")){
            adapter = new ShopsAdapter(CategoriesViewActivity.this, shops, category.getName_en());
        }else {
            adapter = new ShopsAdapter(CategoriesViewActivity.this, shops, category.getName());
        }

        binding.imgNoData.setVisibility(View.GONE);
        binding.txtNoData.setVisibility(View.GONE);
        binding.recyclerShops.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

        /*if ((shops == null || shops.size() == 0) && allShopsList.size() == 0){
            binding.progressBarShops.setVisibility(View.GONE);
            binding.imgNoData.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.VISIBLE);
            binding.recyclerShops.setAdapter(null);
            return;
        }

        int oldS = allShopsList.size();

        if (oldS == 0){
            allShopsList.addAll(shops);
        }else {
            if (oldS >= size){

                if (allShopsList.get(allShopsList.size()-1) == null){
                    allShopsList.remove(allShopsList.size() - 1);

                    adapter.notifyItemRemoved(allShopsList.size());
                    isLoading = false;
                }

                if (shops.size() > 0){
                    if (!(shops.get(shops.size() - 1).getId().equals(allShopsList.get(allShopsList.size() - 1).getId()))){
                        allShopsList.addAll(shops);
                        adapter.notifyItemRangeChanged(oldS, allShopsList.size() - 1);
                    }
                }
            }
        }



        if (adapter == null){
            if(LocalRepo.getLanguage(CategoriesViewActivity.this).equals("en")){
                adapter = new ShopsAdapter(CategoriesViewActivity.this, shops, category.getName_en());
            }else {
                adapter = new ShopsAdapter(CategoriesViewActivity.this, shops, category.getName());
            }
            binding.recyclerShops.setAdapter(adapter);
        }
        binding.imgNoData.setVisibility(View.GONE);
        binding.txtNoData.setVisibility(View.GONE);
        binding.recyclerShops.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        binding.progressBarShops.setVisibility(View.GONE);*/


        if ((shops == null || shops.size() == 0) && allShopsList.size() == 0){
            binding.progressBarShops.setVisibility(View.GONE);
            binding.imgNoData.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.VISIBLE);
            binding.recyclerShops.setAdapter(null);
            return;
        }

        int oldS = allShopsList.size();

        if (oldS == 0){
            allShopsList.addAll(shops);
        }else {
            if (oldS >= size){

                if (allShopsList.get(allShopsList.size()-1) == null){
                    allShopsList.remove(allShopsList.size() - 1);

                    adapter.notifyItemRemoved(allShopsList.size());
                    isLoading = false;
                }

                if (shops.size() > 0){
                    if (!(shops.get(shops.size() - 1).getId().equals(allShopsList.get(allShopsList.size() - 1).getId()))){
                        allShopsList.addAll(shops);
                        adapter.notifyItemRangeChanged(oldS, allShopsList.size() - 1);
                    }
                }
            }
        }

        if (adapter == null || binding.recyclerShops.getAdapter() == null){
            if(LocalRepo.getLanguage(CategoriesViewActivity.this).equals("en")){
                adapter = new ShopsAdapter(CategoriesViewActivity.this, shops, category.getName_en());
            }else {
                adapter = new ShopsAdapter(CategoriesViewActivity.this, shops, category.getName());
            }
            binding.recyclerShops.setAdapter(adapter);
        }

        binding.imgNoData.setVisibility(View.GONE);
        binding.txtNoData.setVisibility(View.GONE);
        //binding.recyclerShops.setAdapter(adapter);

        //adapter.notifyDataSetChanged();
        binding.progressBarShops.setVisibility(View.GONE);


    }


    @Override
    public void OnLoadingAds(boolean loading) {

    }

    @Override
    public void OnAdsGet(List<AdModel> adModelList) {

    }

    @Override
    public void OnAdsCategoryGet(List<AdCategoryModel> adCategoryModelList) {
        if (adCategoryModelList == null){
            return;
        }
        adsSliderAdapter = new AdsCategorySliderAdapter(this, adCategoryModelList);
        binding.imageSlider.setSliderAdapter(adsSliderAdapter);
        adsSliderAdapter.notifyDataSetChanged();
    }

    private void loadMore() {
        allShopsList.add(null);
        adapter.notifyItemInserted(allShopsList.size() - 1);
        // load shops
        shopsViewModel.loadShops(category.getId(), this, page, size);
    }


}