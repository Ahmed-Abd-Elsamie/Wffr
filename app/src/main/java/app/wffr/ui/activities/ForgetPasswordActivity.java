package app.wffr.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityForgetPasswordBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.utils;
import app.wffr.viewmodels.AccountViewModel;

public class ForgetPasswordActivity extends WffrBaseActivity implements LoginRegisterListener {

    private ActivityForgetPasswordBinding binding;
    private AccountViewModel accountViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);

        binding.btnRecover.setOnClickListener(v -> {
            String phone = binding.txtEmail.getText().toString();
            if (TextUtils.isEmpty(phone)){
                Toast.makeText(this, R.string.error_enter_phone, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!utils.isValidMobile(phone)){
                Toast.makeText(this, R.string.error_enter_valid_phone, Toast.LENGTH_SHORT).show();
                return;
            }

            accountViewModel.resetUserPassword(phone, LocalRepo.getLanguage(this));
            binding.progressBarLoading.setVisibility(View.VISIBLE);
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });


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
        if (state.equals("Sended")){
            Toast.makeText(this, getString(R.string.check_new_pass), Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, state, Toast.LENGTH_SHORT).show();
        }
        binding.progressBarLoading.setVisibility(View.GONE);

    }

    @Override
    public void OnApiNotFound(String message) {

    }

    @Override
    public void OnPhoneVerification(String response) {

    }

    @Override
    public void OnCodeResend(String resendCodeResponse) {

    }
}