package app.wffr.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.util.List;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.ProductsAdapter;
import app.wffr.adapters.ProductsAdapterFill;
import app.wffr.databinding.ActivityShopProductsBinding;
import app.wffr.models.Product;
import app.wffr.models.Shop;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;
import app.wffr.viewmodels.GeneralProductViewModel;

public class ShopProductsActivity extends WffrBaseActivity {

    private ActivityShopProductsBinding binding;
    private ProductsAdapterFill totalBillAdapter;
    private ProductsAdapter featuredAdapter;
    private Shop shop;
    private String category;
    private GeneralProductViewModel generalProductViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_products);

        Intent intent = getIntent();
        shop = intent.getParcelableExtra("shop");
        category = intent.getStringExtra("category");

        loadDShopData();
        initRecyclerView();

        generalProductViewModel = new ViewModelProvider(this).get(GeneralProductViewModel.class);
        generalProductViewModel.init(this);

        loadShopProducts(shop.getId());

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void loadShopProducts(String shopId) {
        generalProductViewModel.loadShopProducts(shopId);
        generalProductViewModel.getTotalBillProducts().observe((LifecycleOwner) ShopProductsActivity.this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> list) {
                if (list == null || list.size() == 0){
                    binding.imgNoTotal.setVisibility(View.GONE);
                    binding.txtTotalBill.setText(R.string.no_total_bill);
                    binding.txtTotalBill.setVisibility(View.GONE);
                    binding.recyclerAll.setAdapter(null);
                    return;
                }
                binding.imgNoTotal.setVisibility(View.GONE);
                binding.txtTotalBill.setText(R.string.total_bill);
                totalBillAdapter = new ProductsAdapterFill(ShopProductsActivity.this, list, "");
                binding.recyclerAll.setAdapter(totalBillAdapter);
                totalBillAdapter.notifyDataSetChanged();
            }
        });

        generalProductViewModel.getIsUpdating().observe((LifecycleOwner) ShopProductsActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBarTotalBill);
                }else {
                    hideProgressBar(binding.progressBarTotalBill);
                }
            }
        });

        generalProductViewModel.getFeaturedProducts().observe((LifecycleOwner) ShopProductsActivity.this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> list) {
                if (list == null || list.size() == 0){
                    binding.imgNoFeat.setVisibility(View.GONE);
                    binding.txtFeat.setText(R.string.no_featured_offers);
                    binding.txtFeat.setVisibility(View.GONE);
                    binding.recyclerFeatured.setAdapter(null);
                    return;
                }
                binding.imgNoFeat.setVisibility(View.GONE);
                binding.txtFeat.setText(R.string.featured_offers);
                featuredAdapter = new ProductsAdapter(ShopProductsActivity.this, list, "");
                binding.recyclerFeatured.setAdapter(featuredAdapter);
                featuredAdapter.notifyDataSetChanged();
            }
        });

        generalProductViewModel.getIsUpdating().observe((LifecycleOwner) ShopProductsActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBarFeat);
                }else {
                    hideProgressBar(binding.progressBarFeat);
                }
            }
        });
    }

    private void loadDShopData() {
        if(LocalRepo.getLanguage(ShopProductsActivity.this).equals("en")){
            binding.txtTitle.setText(shop.getNameen());
            binding.txtDesc.setText(shop.getDiscriptionen());
        }else {
            binding.txtTitle.setText(shop.getName());
            binding.txtDesc.setText(shop.getDiscription());
        }
        binding.txtCategoryTitle.setText(category);

        String product_img_url = "https://wffr.app" + shop.getImgCover();
        Glide.with(this).load(product_img_url).into(binding.imgShop);
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {

        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager allLayoutManager = new GridLayoutManager(this, Integer.parseInt(getString(R.string.column_count)), LinearLayoutManager.VERTICAL, false);

        binding.recyclerFeatured.setLayoutManager(featuredLayoutManager);
        binding.recyclerAll.setLayoutManager(allLayoutManager);

        // set animations
        LayoutAnimationController featuredAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_right);
        binding.recyclerFeatured.setLayoutAnimation(featuredAnim);
        binding.recyclerFeatured.scheduleLayoutAnimation();



        LayoutAnimationController allAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerAll.setLayoutAnimation(allAnim);
        binding.recyclerAll.scheduleLayoutAnimation();
    }


}