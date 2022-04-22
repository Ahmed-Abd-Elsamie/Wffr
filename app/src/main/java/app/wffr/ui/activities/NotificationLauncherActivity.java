package app.wffr.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.adapters.NotificationsAdapter;
import app.wffr.databinding.ActivityNotificationsBinding;
import app.wffr.models.NotificationItem;
import app.wffr.models.Product;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.utils;
import app.wffr.viewmodels.NotificationsViewModel;

public class NotificationLauncherActivity extends AppCompatActivity {

    private ActivityNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;
    private NotificationsAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setAppLocale(LocalRepo.getLanguage(this), this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);

        currentUser = LocalRepo.getUserData(this);
        initRecyclerView();

        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationsViewModel.init(this);
        notificationsViewModel.loadNotifications(currentUser.getId());
        notificationsViewModel.loadProductsNotifications(currentUser.getCreationDate());

        notificationsViewModel.getNotifications().observe(this, new Observer<List<NotificationItem>>() {
            @Override
            public void onChanged(List<NotificationItem> notificationItems) {
                if (notificationItems == null || notificationItems.size() == 0){
                    binding.imgNoData.setVisibility(View.VISIBLE);
                    binding.txtNoData.setVisibility(View.VISIBLE);
                    return;
                }

                binding.imgNoData.setVisibility(View.GONE);
                binding.txtNoData.setVisibility(View.GONE);
                adapter = new NotificationsAdapter(NotificationLauncherActivity.this, notificationItems);
                binding.recyclerNotifications.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        notificationsViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
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
        LinearLayoutManager location = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerNotifications.setLayoutManager(location);
        // set animations
        LayoutAnimationController featuredAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_right);
        binding.recyclerNotifications.setLayoutAnimation(featuredAnim);
        binding.recyclerNotifications.scheduleLayoutAnimation();
    }

}