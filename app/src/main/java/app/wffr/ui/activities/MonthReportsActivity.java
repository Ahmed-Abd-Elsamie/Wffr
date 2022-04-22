package app.wffr.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.InvoicesAdapter;
import app.wffr.databinding.ActivityMonthReportsBinding;
import app.wffr.models.Invoice;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.viewmodels.PrevOrdersViewModel;

public class MonthReportsActivity extends WffrBaseActivity {

    private ActivityMonthReportsBinding binding;
    private PrevOrdersViewModel prevOrdersViewModel;
    private User currentUser;
    private InvoicesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month_reports);

        currentUser = LocalRepo.getUserData(this);
        initRecyclerView();

        prevOrdersViewModel = new ViewModelProvider(this).get(PrevOrdersViewModel.class);
        prevOrdersViewModel.init(this);
        prevOrdersViewModel.loadPrevOrders(currentUser.getId());
        prevOrdersViewModel.getPrevOrders().observe(this, new Observer<List<Invoice>>() {
            @Override
            public void onChanged(List<Invoice> orders) {
                if (orders == null || orders.size() == 0){
                    binding.imgNoData.setVisibility(View.VISIBLE);
                    binding.txtNoData.setVisibility(View.VISIBLE);
                    binding.recyclerPreOrders.setAdapter(null);
                    return;
                }
                adapter = new InvoicesAdapter(MonthReportsActivity.this, orders);
                binding.recyclerPreOrders.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerPreOrders.setLayoutManager(layoutManager);
        // set animations
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerPreOrders.setLayoutAnimation(categoriesAnim);
        binding.recyclerPreOrders.scheduleLayoutAnimation();
    }

}