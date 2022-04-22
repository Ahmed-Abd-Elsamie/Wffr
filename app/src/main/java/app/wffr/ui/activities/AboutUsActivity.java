package app.wffr.ui.activities;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityAboutUsBinding;
import app.wffr.models.DashboardSettings;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;
import app.wffr.viewmodels.SettingsViewModel;

public class AboutUsActivity extends WffrBaseActivity {

    private ActivityAboutUsBinding binding;
    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.init(this);
        settingsViewModel.getDashboardSettings().observe(this, new Observer<DashboardSettings>() {
            @Override
            public void onChanged(DashboardSettings jsonObject) {
                if (LocalRepo.getLanguage(AboutUsActivity.this).equals("en")){
                    binding.txtAbout.setText(jsonObject.getAbout_Company_en());
                }else {
                    binding.txtAbout.setText(jsonObject.getAbout_Company());
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}