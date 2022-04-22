package app.wffr.ui.activities;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityPoliciesBinding;
import app.wffr.models.DashboardSettings;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;
import app.wffr.viewmodels.SettingsViewModel;

public class PoliciesActivity extends WffrBaseActivity {

    private ActivityPoliciesBinding binding;
    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policies);


        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.init(this);
        settingsViewModel.getDashboardSettings().observe(this, new Observer<DashboardSettings>() {
            @Override
            public void onChanged(DashboardSettings jsonObject) {
                if (LocalRepo.getLanguage(PoliciesActivity.this).equals("en")){
                    binding.txtPolicy.setText(jsonObject.getConditions_Rules_en());
                }else {
                    binding.txtPolicy.setText(jsonObject.getConditions_Rules());
                }
            }
        });

        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });


    }



}