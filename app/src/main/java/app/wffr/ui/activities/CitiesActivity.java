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
import app.wffr.adapters.CityAdapter;
import app.wffr.databinding.ActivityCitiesBinding;
import app.wffr.models.City;
import app.wffr.viewmodels.CitiesViewModel;

public class CitiesActivity extends WffrBaseActivity {

    private ActivityCitiesBinding binding;
    private CityAdapter adapter;
    private CitiesViewModel citiesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cities);

        initRecyclerView();



        citiesViewModel = new ViewModelProvider(this).get(CitiesViewModel.class);
        citiesViewModel.init(this);
        citiesViewModel.loadCities();
        citiesViewModel.getCities().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                adapter = new CityAdapter(CitiesActivity.this, cities);
                binding.recyclerCities.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/
        citiesViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBarFeat);
                }else {
                    hideProgressBar(binding.progressBarFeat);
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerCities.setLayoutManager(layoutManager);
        // set animations
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerCities.setLayoutAnimation(categoriesAnim);
        binding.recyclerCities.scheduleLayoutAnimation();
    }


}