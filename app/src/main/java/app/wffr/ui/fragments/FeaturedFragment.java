package app.wffr.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseFragment;
import app.wffr.adapters.FeaturedAdsAdapter;
import app.wffr.adapters.ProductsAdapterFill;
import app.wffr.databinding.FeaturedFragmentBinding;
import app.wffr.listeners.FeaturedProductListener;
import app.wffr.models.FeaturedSlider;
import app.wffr.models.Product;
import app.wffr.utils.Constants;
import app.wffr.utils.utils;
import app.wffr.viewmodels.FeaturedViewModel;

public class FeaturedFragment extends WffrBaseFragment implements FeaturedProductListener {

    private FeaturedViewModel featuredViewModel;
    private FeaturedFragmentBinding binding;
    private ProductsAdapterFill featuredAdapter;
    private AppCompatActivity context;
    private FeaturedAdsAdapter featuredAdsAdapter;

    public static FeaturedFragment newInstance() {
        return new FeaturedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.featured_fragment, container, false);
        context = (AppCompatActivity) getActivity();

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();

        // initialize view model
        featuredViewModel = new ViewModelProvider(this).get(FeaturedViewModel.class);
        featuredViewModel.init(context);

        // loading data ==> pass user id or 0 for not login user
        featuredViewModel.loadFeatured(MainActivity.user == null ? 0 + "" : MainActivity.user.getId(), this, Constants.LOAD);

        // handle slider
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();


        binding.pullToRefresh.setOnRefreshListener(() -> {
            refreshData();
        });


    }

    private void refreshData() {
        featuredViewModel.loadFeatured(MainActivity.user == null ? 0 + "" : MainActivity.user.getId(), this, Constants.REFRESH);
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager featuredLayoutManager = new GridLayoutManager(context, Integer.parseInt(getString(R.string.column_count)), LinearLayoutManager.VERTICAL, false);
        binding.recyclerFeatured.setLayoutManager(featuredLayoutManager);
        // set animations
        /*LayoutAnimationController featuredAnim = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_slide_right);
        binding.recyclerFeatured.setLayoutAnimation(featuredAnim);
        binding.recyclerFeatured.scheduleLayoutAnimation();*/
    }

    @Override
    public void OnLoadingData(boolean loading) {
        if (loading){
            showProgressBar(binding.progressBarFeat);
        }else {
            hideProgressBar(binding.progressBarFeat);
        }
        binding.pullToRefresh.setRefreshing(loading);
    }

    @Override
    public void OnProductsGet(List<Product> products, List<FeaturedSlider> adsList) {

        if (adsList != null){
            featuredAdsAdapter = new FeaturedAdsAdapter(context, adsList);
            binding.imageSlider.setSliderAdapter(featuredAdsAdapter);
            featuredAdsAdapter.notifyDataSetChanged();
        }

        if (products == null || products.size() == 0){
            binding.recyclerFeatured.setAdapter(null);
            binding.recyclerFeatured.setVisibility(View.GONE);
            binding.txtFeat.setText(R.string.no_featured_offers);
            binding.txtFeat.setVisibility(View.VISIBLE);
            binding.imgNoFeat.setVisibility(View.VISIBLE);
            return;
        }

        binding.recyclerFeatured.setVisibility(View.VISIBLE);
        binding.txtFeat.setText(R.string.featured_offers);
        binding.imgNoFeat.setVisibility(View.GONE);
        binding.txtFeat.setVisibility(View.GONE);
        featuredAdapter = new ProductsAdapterFill(context, products, "");
        binding.recyclerFeatured.setAdapter(featuredAdapter);
        featuredAdapter.notifyDataSetChanged();
    }


}