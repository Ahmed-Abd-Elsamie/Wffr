package app.wffr.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivitySettingsBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.listeners.UserLoginRegisterListener;
import app.wffr.models.City;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.utils;
import app.wffr.viewmodels.AccountViewModel;

public class SettingsActivity extends WffrBaseActivity implements LoginRegisterListener {

    private ActivitySettingsBinding binding;
    private City city;
    private AccountViewModel accountViewModel;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        city = LocalRepo.getCity(this);
        if (city == null){
            binding.txtCity.setText(R.string.no_sel_city);
        }else {
            if (LocalRepo.getLanguage(this).equals("en")){
                binding.txtCity.setText(city.getNameen());
            }else {
                binding.txtCity.setText(city.getName());
            }
        }

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);

        binding.btnChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, CitiesActivity.class));
            }
        });

        binding.btnChangePass.setOnClickListener(v -> {
            if (!isLoading){
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                intent.putExtra("forget", false);
                startActivity(intent);
            }
        });

        binding.btnForgetPass.setOnClickListener(v -> {
            accountViewModel.resendVerificationCode(LocalRepo.getUserData(this).getPhone());
            binding.progressBar.setVisibility(View.VISIBLE);
        });

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        if (LocalRepo.getLanguage(this).equals("en")){
            binding.txtLang.setText("English");
        }else {
            binding.txtLang.setText("العربيه");
        }

        binding.btnChangeLanguage.setOnClickListener(v -> {
            chooseLang();
        });

        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        city = LocalRepo.getCity(this);
        if (city == null){
            binding.txtCity.setText(R.string.no_sel_city);
        }else {
            if (LocalRepo.getLanguage(this).equals("en")){
                binding.txtCity.setText(city.getNameen());
            }else {
                binding.txtCity.setText(city.getName());
            }
        }
    }


    private void chooseLang(){
        AlertDialog builderSingle = new AlertDialog.Builder(SettingsActivity.this).create();
        builderSingle.setIcon(R.drawable.circular_logo);
        builderSingle.setTitle(getString(R.string.select_language));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_language, null);

        builderSingle.setView(view);

        RadioButton en = view.findViewById(R.id.radio_en);
        RadioButton ar = view.findViewById(R.id.radio_ar);

        if (LocalRepo.getLanguage(this).equals("en")){
            en.setChecked(true);
        }else {
            ar.setChecked(true);
        }


        en.setOnClickListener(v -> {
            builderSingle.dismiss();
            LocalRepo.saveLanguage(SettingsActivity.this, "en");
            utils.setAppLocale("en", SettingsActivity.this);
            Intent intent = new Intent(SettingsActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        });

        ar.setOnClickListener(v -> {
            builderSingle.dismiss();
            LocalRepo.saveLanguage(SettingsActivity.this, "ar");
            utils.setAppLocale("ar", SettingsActivity.this);
            Intent intent = new Intent(SettingsActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        });

        /*builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0){

                }else {

                }


            }
        });*/
        builderSingle.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnFailure(String message) {

    }

    @Override
    public void OnUserRegister(User user) {

    }

    @Override
    public void OnUserUpdated(String state) {

    }

    @Override
    public void OnCodeSent(String state) {

    }

    @Override
    public void OnApiNotFound(String message) {

    }

    @Override
    public void OnPhoneVerification(String response) {
        if (response.equals("ok")){
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            intent.putExtra("forget", true);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
        }
        isLoading = false;
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void OnCodeResend(String resendCodeResponse) {
        if (resendCodeResponse.equals("ok")){
            showCodeDialog(getString(R.string.verify_code));
        }
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showCodeDialog(String message) {

        AlertDialog builder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_code, null);
        builder.setView(view);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setCancelable(false);
        Button btnOk = view.findViewById(R.id.btn_ok);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        TextView textView = view.findViewById(R.id.txt_state);
        EditText txtCode = view.findViewById(R.id.txt_code);

        textView.setText(message);

        btnCancel.setOnClickListener(v -> {
            builder.dismiss();
        });

        btnOk.setOnClickListener(v -> {
            String confCode = txtCode.getText().toString();
            if (TextUtils.isEmpty(confCode)){
                Toast.makeText(this, getString(R.string.verify_empty_code), Toast.LENGTH_SHORT).show();
            }else {
                accountViewModel.verifyUserPhone(Integer.parseInt(LocalRepo.getUserData(SettingsActivity.this).getId()), Integer.parseInt(confCode));
                binding.progressBar.setVisibility(View.VISIBLE);
                isLoading = true;
                builder.dismiss();
            }
        });

        builder.show();
    }


}