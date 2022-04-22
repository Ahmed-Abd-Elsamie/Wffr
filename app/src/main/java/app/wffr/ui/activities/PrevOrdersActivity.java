package app.wffr.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.PrevProductsAdapter;
import app.wffr.databinding.ActivityPrevOrdersBinding;
import app.wffr.models.Product;
import app.wffr.viewmodels.PrevOrdersViewModel;

public class PrevOrdersActivity extends WffrBaseActivity {

    private ActivityPrevOrdersBinding binding;
    private PrevOrdersViewModel prevOrdersViewModel;
    private PrevProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prev_orders);

        initRecyclerView();

        prevOrdersViewModel = new ViewModelProvider(this).get(PrevOrdersViewModel.class);
        prevOrdersViewModel.init(this);
        prevOrdersViewModel.loadPrevOrdersProducts(MainActivity.user.getId());
        prevOrdersViewModel.getPrevOrdersProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> orders) {
                if (orders == null || orders.size() == 0){
                    binding.imgNoData.setVisibility(View.VISIBLE);
                    binding.txtNoData.setVisibility(View.VISIBLE);
                    return;
                }
                adapter = new PrevProductsAdapter(PrevOrdersActivity.this, orders, "");
                binding.recyclerPreOrders.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        prevOrdersViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBar);
                }else {
                    hideProgressBar(binding.progressBar);
                }
            }
        });


        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new GridLayoutManager(this, Integer.parseInt(getString(R.string.column_count)), LinearLayoutManager.VERTICAL, false);
        binding.recyclerPreOrders.setLayoutManager(layoutManager);
        // set animations
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerPreOrders.setLayoutAnimation(categoriesAnim);
        binding.recyclerPreOrders.scheduleLayoutAnimation();
    }

}