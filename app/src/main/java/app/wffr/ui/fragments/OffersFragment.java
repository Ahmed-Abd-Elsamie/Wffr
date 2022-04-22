package app.wffr.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import java.util.List;

import app.wffr.R;
import app.wffr.WffrBaseFragment;
import app.wffr.adapters.AdsSliderAdapter;
import app.wffr.adapters.CategoriesAdapter;
import app.wffr.databinding.OffersFragmentBinding;
import app.wffr.listeners.AdsListener;
import app.wffr.listeners.CategoriesListener;
import app.wffr.models.AdCategoryModel;
import app.wffr.models.AdModel;
import app.wffr.models.Category;
import app.wffr.utils.Constants;
import app.wffr.viewmodels.AdsViewModel;
import app.wffr.viewmodels.CategoriesViewModel;

public class OffersFragment extends WffrBaseFragment implements CategoriesListener, AdsListener {

    private OffersFragmentBinding binding;
    private AppCompatActivity context;
    private CategoriesAdapter categoriesAdapter;
    private AdsViewModel adsViewModel;
    private CategoriesViewModel categoriesViewModel;
    private AdsSliderAdapter adsSliderAdapter;

    public static OffersFragment newInstance() {
        return new OffersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.offers_fragment, container, false);
        context = (AppCompatActivity) getActivity();

        initRecyclerView();

        // creating view models
        adsViewModel = new ViewModelProvider(this).get(AdsViewModel.class);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        // initialize view models
        adsViewModel.init(context);
        categoriesViewModel.init(context);

        // load ads
        adsViewModel.loadAds(Constants.LOAD, this);
        // load categories
        categoriesViewModel.loadCategories(Constants.LOAD, this);

        // handle slider
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();


        binding.pullToRefresh.setOnRefreshListener(() -> {
            adsViewModel.loadAds(Constants.REFRESH, OffersFragment.this);
            categoriesViewModel.loadCategories(Constants.REFRESH, OffersFragment.this);
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
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerCategory.setLayoutManager(categoryLayoutManager);
    }


    @Override
    public void OnLoadingCategories(boolean loading) {
        if (loading){
            showProgressBar(binding.progressBarCategories);
        }else {
            hideProgressBar(binding.progressBarCategories);
        }
    }

    @Override
    public void OnCategoriesGet(List<Category> categoryList) {
        categoriesAdapter = new CategoriesAdapter(context, categoryList);
        binding.recyclerCategory.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();
    }


    @Override
    public void OnLoadingAds(boolean loading) {
        binding.pullToRefresh.setRefreshing(loading);
    }

    @Override
    public void OnAdsGet(List<AdModel> adModelList) {
        adsSliderAdapter = new AdsSliderAdapter(context, adModelList);
        binding.imageSlider.setSliderAdapter(adsSliderAdapter);
        adsSliderAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnAdsCategoryGet(List<AdCategoryModel> adCategoryModelList) {

    }


}