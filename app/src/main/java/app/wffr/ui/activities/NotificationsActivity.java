package app.wffr.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.NotificationsAdapter;
import app.wffr.databinding.ActivityNotificationsBinding;
import app.wffr.models.FirebaseNotificationModel;
import app.wffr.models.NotificationItem;
import app.wffr.models.Product;
import app.wffr.viewmodels.NotificationsViewModel;

public class NotificationsActivity extends WffrBaseActivity {

    private ActivityNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;
    private NotificationsAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private FirebaseNotificationModel firebaseUserData;
    private FirebaseNotificationModel firebaseGenData;
    private DatabaseReference reference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        firebaseUserData = intent.getParcelableExtra("my_notify");
        firebaseGenData = intent.getParcelableExtra("general_notify");

        Map map = new HashMap();
        map.put("general", firebaseGenData.getGeneral());
        map.put("male", firebaseGenData.getMale());
        map.put("female", firebaseGenData.getFemale());
        reference.child("users").child("user_" + MainActivity.user.getId()).updateChildren(map);

        initRecyclerView();

        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationsViewModel.init(this);
        notificationsViewModel.loadNotifications(MainActivity.user.getId());
        notificationsViewModel.loadProductsNotifications(MainActivity.user.getCreationDate());

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
                adapter = new NotificationsAdapter(NotificationsActivity.this, notificationItems);
                binding.recyclerNotifications.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                /*notificationsViewModel.getProductsNotifications().observe(NotificationsActivity.this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {


                        for (Product product : products){
                            notificationItems.add(new NotificationItem(
                                    "0",
                                    product.get_name(),
                                    product.get_nameen(),
                                    product.get_Disciptionar(),
                                    product.get_Disciptionen(),
                                    "",
                                    product.get_img(),
                                    "0", // it's a product
                                    "",
                                    null,
                                    product
                            ));
                        }
                        binding.recyclerNotifications.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });*/
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