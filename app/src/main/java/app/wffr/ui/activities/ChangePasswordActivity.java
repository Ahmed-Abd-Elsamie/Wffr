package app.wffr.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityChangePasswordBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.listeners.UserLoginRegisterListener;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.utils;
import app.wffr.viewmodels.AccountViewModel;

public class ChangePasswordActivity extends WffrBaseActivity implements LoginRegisterListener {

    private ActivityChangePasswordBinding binding;
    private AccountViewModel accountViewModel;
    private User currentUser;
    private String userImage = "";
    private boolean isForget = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);

        Intent intent = getIntent();
        isForget = intent.getBooleanExtra("forget", false);
        currentUser = LocalRepo.getUserData(this);

        if (isForget){
            binding.oldPassContainer.setVisibility(View.GONE);
            binding.txtNewPass.setEnabled(true);
            binding.txtConfirmPass.setEnabled(true);
        }else {
            binding.txtOldPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (currentUser.getPassword().equals(binding.txtOldPass.getText().toString())){
                        binding.txtNewPass.setEnabled(true);
                        binding.txtConfirmPass.setEnabled(true);
                    }else {
                        binding.txtNewPass.setEnabled(false);
                        binding.txtConfirmPass.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);
        /*accountViewModel.init(this, new UserLoginRegisterListener() {
            @Override
            public void onComplete(@NonNull User user, boolean isSuccess) {
                // for login and register only
            }

            @Override
            public void onUpdateComplete(@NonNull String state, boolean isSuccess) {
                binding.progressBar.setVisibility(View.GONE);
                if (isSuccess){
                    saveUserDataLocal();
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.update_error), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });*/

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.btnSave.setOnClickListener(v -> {
            validateData();
        });

        binding.btnShowPass.setOnClickListener(v -> {
            binding.txtOldPass.setTransformationMethod(null);
            binding.txtNewPass.setTransformationMethod(null);
            binding.txtConfirmPass.setTransformationMethod(null);
            binding.btnShowPass.setVisibility(View.GONE);
            binding.btnHidePass.setVisibility(View.VISIBLE);
        });

        binding.btnHidePass.setOnClickListener(v -> {
            binding.txtOldPass.setTransformationMethod(new PasswordTransformationMethod());
            binding.txtNewPass.setTransformationMethod(new PasswordTransformationMethod());
            binding.txtConfirmPass.setTransformationMethod(new PasswordTransformationMethod());
            binding.btnHidePass.setVisibility(View.GONE);
            binding.btnShowPass.setVisibility(View.VISIBLE);
        });


    }

    private void saveUserDataLocal() {

    }

    private void validateData() {

        if (!utils.internetIsConnected()){
            utils.showInternetDialog(this);
            return;
        }

        String old_pass = binding.txtOldPass.getText().toString();
        String new_pass = binding.txtNewPass.getText().toString();
        String conf_new_pass = binding.txtConfirmPass.getText().toString();

        if (TextUtils.isEmpty(old_pass) && !isForget){
            Toast.makeText(this, getString(R.string.change_pass_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!old_pass.equals(currentUser.getPassword()) && !isForget){
            Toast.makeText(this, getString(R.string.old_pass_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(new_pass)){
            Toast.makeText(this, getString(R.string.new_pass_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(conf_new_pass)){
            Toast.makeText(this, getString(R.string.error_enter_conf_pass), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!new_pass.equals(conf_new_pass)){
            Toast.makeText(this, R.string.error_same_pass, Toast.LENGTH_SHORT).show();
            return;
        }

        if (new_pass.length() < 6){
            Toast.makeText(this, getString(R.string.error_pass_length), Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setPassword(new_pass);
        userImage = currentUser.getImg();
        User newUser = currentUser;
        newUser.setImg(""); // don't change image

        accountViewModel.updateUser(newUser);
        binding.progressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void OnFailure(String message) {

    }

    @Override
    public void OnUserRegister(User user) {

    }

    @Override
    public void OnUserUpdated(String state) {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(ChangePasswordActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
        currentUser.setImg(userImage);
        LocalRepo.saveUserData(this, currentUser);
        MainActivity.user = currentUser;
        finish();
    }

    @Override
    public void OnCodeSent(String state) {

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