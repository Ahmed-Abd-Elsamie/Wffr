package app.wffr.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityLoginBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.utils;
import app.wffr.viewmodels.AccountViewModel;

public class LoginActivity extends WffrBaseActivity implements LoginRegisterListener {

    private ActivityLoginBinding binding;
    private AccountViewModel accountViewModel;
    private String code = "";
    private User resetUser = null;
    private final int MESSAGE_DIALOG = 0;
    private final int RESET_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loadAnimation();

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);

        binding.btnRegisterPage.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            validateUserData();
        });

        binding.txtForgetPass.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        });

        binding.btnShowPass.setOnClickListener(v -> {
            binding.txtPass.setTransformationMethod(null);
            binding.btnShowPass.setVisibility(View.GONE);
            binding.btnHidePass.setVisibility(View.VISIBLE);
        });

        binding.btnHidePass.setOnClickListener(v -> {
            binding.txtPass.setTransformationMethod(new PasswordTransformationMethod());
            binding.btnHidePass.setVisibility(View.GONE);
            binding.btnShowPass.setVisibility(View.VISIBLE);
        });

    }

    private void loadAnimation() {
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.loginContainer.setLayoutAnimation(categoriesAnim);
        binding.loginContainer.scheduleLayoutAnimation();
        binding.inputContainer.setLayoutAnimation(categoriesAnim);
        binding.inputContainer.scheduleLayoutAnimation();
    }

    private void validateUserData() {

        String email = binding.txtEmail.getText().toString();
        String pass1 = binding.txtPass.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, R.string.error_enter_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isEmailValid(email)){
            Toast.makeText(this, R.string.error_enter_valid_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass1)){
            Toast.makeText(this, R.string.error_enter_pass, Toast.LENGTH_SHORT).show();
            return;
        }

        accountViewModel.loginUser(email, pass1);
        binding.progressBarLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void showRegisterMsg(String msg, int state) {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_message, null);
        builder.setView(view);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = view.findViewById(R.id.btn_done);
        TextView txtMsg = view.findViewById(R.id.txt_message);
        ImageView imgMsg = view.findViewById(R.id.img_message);

        txtMsg.setText(msg);
        if (state == MESSAGE_DIALOG){ // error
            imgMsg.setImageResource(R.drawable.ic_round_close_green_35);
        }


        btnOk.setOnClickListener(v -> {
            builder.dismiss();
        });

        builder.show();
    }


    @Override
    public void OnFailure(String message) {
        binding.progressBarLoading.setVisibility(View.GONE);
        showRegisterMsg(getString(R.string.error_login), MESSAGE_DIALOG);
    }

    @Override
    public void OnUserRegister(User user) {
        binding.progressBarLoading.setVisibility(View.GONE);
        if (user.isEnabled()){
            saveUserLocale(user);
        }else {
            Intent intent = new Intent(LoginActivity.this, VerifyEmailActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "login");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void OnUserUpdated(String state) {

    }

    @Override
    public void OnCodeSent(String state) {
        /*this.code = code;
        Toast.makeText(this, "Check your Phone for Code", Toast.LENGTH_SHORT).show();
        binding.progressBarLoading.setVisibility(View.GONE);
        resetUser = user;*/
    }

    @Override
    public void OnApiNotFound(String message) {

    }

    @Override
    public void OnPhoneVerification(String response) {

    }

    @Override
    public void OnCodeResend(String resendCodeResponse) {
        //Toast.makeText(this, resendCodeResponse, Toast.LENGTH_SHORT).show();
    }

    private void saveUserLocale(User user) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LocalRepo.saveUserData(LoginActivity.this, user);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });


    }


}