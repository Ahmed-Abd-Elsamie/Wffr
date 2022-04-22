package app.wffr.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.FavProductsAdapter;
import app.wffr.adapters.ShopsAdapter;
import app.wffr.databinding.ActivityFavoritesBinding;
import app.wffr.listeners.FavoriteListener;
import app.wffr.models.Product;
import app.wffr.models.Shop;
import app.wffr.repositories.LocalRepo;
import app.wffr.viewmodels.FavoritesViewModel;

public class FavoritesActivity extends WffrBaseActivity implements FavoriteListener {

    private ActivityFavoritesBinding binding;
    private FavoritesViewModel favoritesViewModel;
    private FavProductsAdapter adapter;
    private boolean isPaused = false;
    private Integer SIZE = null;
    private int page = 0;
    private List<Product> allProducts;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);

        allProducts = new ArrayList<>();

        initRecyclerView();

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        favoritesViewModel.init(this, this);

        favoritesViewModel.loadFavorites(LocalRepo.getUserData(this).getId(), page, SIZE);

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

      /*  binding.recyclerFav.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (allProducts.size() >= SIZE && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == allProducts.size() - 1) {
                        //bottom of list!
                        page++;
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
*/
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new GridLayoutManager(this, Integer.parseInt(getString(R.string.column_count)), LinearLayoutManager.VERTICAL, false);
        binding.recyclerFav.setLayoutManager(layoutManager);
        // set animations
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerFav.setLayoutAnimation(categoriesAnim);
        binding.recyclerFav.scheduleLayoutAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused){
            favoritesViewModel.loadFavorites(LocalRepo.getUserData(this).getId(), page, SIZE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    public void OnFavoritesGet(List<Product> productList) {
        if (productList == null || productList.size() == 0){
            binding.imgNoData.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.VISIBLE);
            binding.recyclerFav.setAdapter(null);
            return;
        }
        adapter = new FavProductsAdapter(FavoritesActivity.this, productList, favoritesViewModel, this);
        binding.recyclerFav.setAdapter(adapter);
        adapter.notifyDataSetChanged();


       /* if ((productList == null || productList.size() == 0) && allProducts.size() == 0){
            binding.progressBarFav.setVisibility(View.GONE);
            binding.imgNoData.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.VISIBLE);
            binding.recyclerFav.setAdapter(null);
            return;
        }


        int oldS = allProducts.size();

        if (oldS == 0){
            allProducts.addAll(productList);
        }else {
            if (oldS >= SIZE){

                if (allProducts.get(allProducts.size()-1) == null){
                    allProducts.remove(allProducts.size() - 1);

                    adapter.notifyItemRemoved(allProducts.size());
                    isLoading = false;
                }

                if (productList.size() > 0){
                    if (!(productList.get(productList.size() - 1).get_id().equals(allProducts.get(allProducts.size() - 1).get_id()))){
                        allProducts.addAll(productList);
                        adapter.notifyItemRangeChanged(oldS, allProducts.size() - 1);
                    }
                }
            }
        }

        if (adapter == null || binding.recyclerFav.getAdapter() == null){
            adapter = new FavProductsAdapter(FavoritesActivity.this, allProducts, favoritesViewModel, this);
            binding.recyclerFav.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        binding.imgNoData.setVisibility(View.GONE);
        binding.txtNoData.setVisibility(View.GONE);
        binding.progressBarFav.setVisibility(View.GONE);*/
    }

    @Override
    public void OnAddRemoveFavorites(String response, Product product) {
        Toast.makeText(this, getString(R.string.fav_removed), Toast.LENGTH_SHORT).show();
        allProducts.clear();
        page = 0;
        //binding.recyclerFav.setAdapter(null);
        favoritesViewModel.loadFavorites(LocalRepo.getUserData(this).getId(), page, SIZE);
    }

    @Override
    public void OnLoading(boolean loading) {
        if (loading){
            showProgressBar(binding.progressBarFav);
        }else {
            hideProgressBar(binding.progressBarFav);
        }
    }

    private void loadMore() {
        allProducts.add(null);
        adapter.notifyItemInserted(allProducts.size() - 1);
        // load products
        favoritesViewModel.loadFavorites(LocalRepo.getUserData(this).getId(), page, SIZE);
    }

}