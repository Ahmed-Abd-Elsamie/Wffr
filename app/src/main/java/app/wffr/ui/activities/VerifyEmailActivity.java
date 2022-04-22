package app.wffr.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.databinding.ActivityVerifyEmailBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.viewmodels.AccountViewModel;

public class VerifyEmailActivity extends AppCompatActivity implements LoginRegisterListener {

    private ActivityVerifyEmailBinding binding;
    private AccountViewModel accountViewModel;
    private DatabaseReference reference;
    private User currentUser;
    private CountDownTimer countDownTimer;
    private boolean isFirst = true;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_email);

        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");
        type = intent.getStringExtra("type");

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);

        if (type.equals("login")){
            resendCode();
        }

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnResendVerify.setOnClickListener(v -> {
            binding.btnVerify.setVisibility(View.VISIBLE);
            binding.btnResendVerify.setVisibility(View.GONE);
            //binding.btnResendVerify.setClickable(false);
            //binding.btnResendVerify.setEnabled(false);
            resendCode();
        });

        countDownTimer = new CountDownTimer(60000, 1000){
            int time = 59;
            @Override
            public void onTick(long millisUntilFinished) {
                binding.btnResendVerify.setText(" " + getString(R.string.resend) + " " + time);
                time--;
            }

            @Override
            public void onFinish() {
                binding.btnResendVerify.setEnabled(true);
                binding.btnResendVerify.setClickable(true);
                binding.btnResendVerify.setText(getString(R.string.resend));
            }
        }.start();

    }

    private void resendCode() {
        binding.btnResendVerify.setVisibility(View.GONE);
        binding.btnVerify.setVisibility(View.VISIBLE);
        accountViewModel.resendVerificationCode(currentUser.getPhone());
        binding.progressBarLoading.setVisibility(View.VISIBLE);
    }

    private void validateData() {

        String code = binding.txtCode.getText().toString().trim();

        if (TextUtils.isEmpty(code)){
            Toast.makeText(this, getString(R.string.verify_empty_code), Toast.LENGTH_SHORT).show();
            return;
        }

        if (code.length() < 5){
            Toast.makeText(this, "Enter a valid code", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBarLoading.setVisibility(View.VISIBLE);
        accountViewModel.verifyUserPhone(Integer.parseInt(currentUser.getId()), Integer.parseInt(code));
        binding.btnVerify.setVisibility(View.GONE);
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
    public void OnPhoneVerification(String string) {
        if (string.equals("ok")){
            saveUserLocale(currentUser);
        }else {
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
            binding.btnVerify.setVisibility(View.VISIBLE);
        }

        binding.progressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void OnCodeResend(String resendCodeResponse) {
        //Toast.makeText(this, resendCodeResponse, Toast.LENGTH_SHORT).show();
        binding.progressBarLoading.setVisibility(View.GONE);
    }

    private void saveUserLocale(User user) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LocalRepo.saveUserData(VerifyEmailActivity.this, user);
                MainActivity.user = user;
                saveUserNotifications(user);
            }
        });
    }

    private void saveUserNotifications(User user) {
        reference.child("general_count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot genSnap) {
                Map<String, Object> firebaseGenData;
                firebaseGenData = (Map<String, Object>)genSnap.getValue();
                int male = Integer.parseInt(firebaseGenData.get("male").toString());
                int female = Integer.parseInt(firebaseGenData.get("female").toString());
                int gen_count = Integer.parseInt(firebaseGenData.get("general").toString());

                Map map = new HashMap();
                map.put("general", gen_count + "");
                map.put("male", male + "");
                map.put("female", female + "");
                reference.child("users").child("user_" + user.getId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        binding.progressBarLoading.setVisibility(View.GONE);
                        Toast.makeText(VerifyEmailActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(VerifyEmailActivity.this, MainActivity.class));
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}