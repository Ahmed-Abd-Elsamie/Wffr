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
import app.wffr.adapters.SocialAdapter;
import app.wffr.databinding.ActivityContactUsBinding;
import app.wffr.models.SocialModel;
import app.wffr.viewmodels.SocialViewModel;

public class ContactUsActivity extends WffrBaseActivity {

    private ActivityContactUsBinding binding;
    private SocialViewModel socialViewModel;
    private SocialAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);

        initRecyclerView();

        socialViewModel = new ViewModelProvider(this).get(SocialViewModel.class);
        socialViewModel.init(this);
        socialViewModel.getSocialContact().observe(this, new Observer<List<SocialModel>>() {
            @Override
            public void onChanged(List<SocialModel> socialModels) {
                adapter = new SocialAdapter(ContactUsActivity.this, socialModels);
                binding.recyclerSocial.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/
        socialViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
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
        binding.recyclerSocial.setLayoutManager(layoutManager);
        // set animations
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerSocial.setLayoutAnimation(categoriesAnim);
        binding.recyclerSocial.scheduleLayoutAnimation();
    }

}