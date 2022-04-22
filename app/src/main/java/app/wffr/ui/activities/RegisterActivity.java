package app.wffr.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityRegisterBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.listeners.UserLoginRegisterListener;
import app.wffr.models.DashboardSettings;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;
import app.wffr.utils.utils;
import app.wffr.viewmodels.AccountViewModel;
import app.wffr.viewmodels.SettingsViewModel;

public class RegisterActivity extends WffrBaseActivity implements LoginRegisterListener {

    private ActivityRegisterBinding binding;
    private AccountViewModel accountViewModel;
    private int GALLERY_REQUEST = 100;
    private Uri imgUri;
    private Bitmap bitmap;
    private SettingsViewModel settingsViewModel;
    private String policy = "";
    private DatabaseReference reference;
    private boolean gender = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        reference = FirebaseDatabase.getInstance().getReference();

        loadAnimation();

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.init(this);
        settingsViewModel.getDashboardSettings().observe(this, new Observer<DashboardSettings>() {
            @Override
            public void onChanged(DashboardSettings jsonObject) {
                if (LocalRepo.getLanguage(RegisterActivity.this).equals("en")){
                    policy = jsonObject.getConditions_Rules_en();
                }else {
                    policy = jsonObject.getConditions_Rules();
                }
            }
        });

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);

        binding.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnRegister.setOnClickListener(v -> {
            validateUserData();
        });

        binding.btnUploadPhoto.setOnClickListener(v -> {
            viewGallery();
        });

        binding.btnShowPass.setOnClickListener(v -> {
            binding.txtPass.setTransformationMethod(null);
            binding.txtConfirmPass.setTransformationMethod(null);
            binding.btnShowPass.setVisibility(View.GONE);
            binding.btnHidePass.setVisibility(View.VISIBLE);
        });

        binding.btnHidePass.setOnClickListener(v -> {
            binding.txtPass.setTransformationMethod(new PasswordTransformationMethod());
            binding.txtConfirmPass.setTransformationMethod(new PasswordTransformationMethod());
            binding.btnHidePass.setVisibility(View.GONE);
            binding.btnShowPass.setVisibility(View.VISIBLE);
        });

        binding.btnTerms.setOnClickListener(v -> {
            showPolicyDialog();
        });

        binding.userGender.setOnClickListener(v -> {
            chooseGender();
        });

        binding.termsCheck.setOnClickListener(v -> {
            showPolicyDialog();
        });

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
        if (state == 0){ // error
            imgMsg.setImageResource(R.drawable.ic_round_close_green_35);
        }


        btnOk.setOnClickListener(v -> {
            builder.dismiss();
        });

        builder.show();
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
                        Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPolicyDialog() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_policy, null);
        builder.setView(view);

        Button btnAgree = view.findViewById(R.id.btn_agree);
        Button btnDisAgree = view.findViewById(R.id.btn_disagree);

        TextView txt = view.findViewById(R.id.txt_policy);
        txt.setText(policy);

        binding.termsCheck.setChecked(false);

        btnAgree.setOnClickListener(v -> {
            binding.termsCheck.setChecked(true);
            builder.dismiss();
        });

        btnDisAgree.setOnClickListener(v -> {
            binding.termsCheck.setChecked(false);
            builder.dismiss();
        });

        if (!utils.internetIsConnected()){
            utils.showInternetDialog(this);
        }

        builder.show();

    }

    private void validateUserData() {

        String name = binding.txtName.getText().toString();
        String email = binding.txtEmail.getText().toString();
        String pass1 = binding.txtPass.getText().toString();
        String pass2 = binding.txtConfirmPass.getText().toString();
        String phone = binding.txtPhone.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, R.string.error_enter_name, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidFirstChar(name)){
            Toast.makeText(this, R.string.first_char_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidWord(name)){
            Toast.makeText(this, R.string.error_enter_valid_name, Toast.LENGTH_SHORT).show();
            return;
        }

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

        if (pass1.length() < 6){
            Toast.makeText(this, R.string.error_pass_length, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass2)){
            Toast.makeText(this, R.string.error_enter_conf_pass, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass1.equals(pass2)){
            Toast.makeText(this, R.string.error_same_pass, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, R.string.error_enter_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidMobile(phone)){
            Toast.makeText(this, R.string.error_enter_valid_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        if (bitmap == null){
            Toast.makeText(this, R.string.error_enter_image, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!binding.termsCheck.isChecked()){
            Toast.makeText(this, R.string.error_confirm_policy, Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(
                0 + "",
                name,
                email,
                pass1,
                phone,
                "",
                false
        );
        user.setGender(gender);

        String userImg = imgToString();
        user.setImg(userImg);

        binding.progressBarLoading.setVisibility(View.VISIBLE);
        accountViewModel.registerUser(user);
    }

    private void viewGallery() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i , GALLERY_REQUEST);
    }

    private void loadAnimation() {
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.inputContainer.setLayoutAnimation(categoriesAnim);
        binding.inputContainer.scheduleLayoutAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST){
            imgUri = data.getData();
            binding.imgProfile.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            }catch (Exception e){
                System.out.println("UPLOAD ERROR : " + e.getMessage());
            }
        }
    }

    private String imgToString(){
        if (bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] image = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(image, Base64.DEFAULT);
        }else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private void chooseGender(){
        AlertDialog builderSingle = new AlertDialog.Builder(RegisterActivity.this).create();
        builderSingle.setIcon(R.drawable.circular_logo);
        builderSingle.setTitle(getString(R.string.gender));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_language, null);

        builderSingle.setView(view);

        RadioButton male = view.findViewById(R.id.radio_en);
        RadioButton female = view.findViewById(R.id.radio_ar);

        male.setText(getString(R.string.male));
        female.setText(getString(R.string.female));


        if (gender){
            male.setChecked(true);
        }else {
            female.setChecked(true);
        }

        male.setOnClickListener(v -> {
            gender = true;
            binding.txtGender.setText(getString(R.string.male));
            builderSingle.dismiss();
        });

        female.setOnClickListener(v -> {
            gender = false;
            binding.txtGender.setText(getString(R.string.female));
            builderSingle.dismiss();
        });

        builderSingle.show();
    }

    @Override
    public void OnFailure(String message) {
        binding.progressBarLoading.setVisibility(View.GONE);
        showRegisterMsg(getString(R.string.phone_email_error), 0);
    }

    @Override
    public void OnUserRegister(User user) {
        binding.progressBarLoading.setVisibility(View.GONE);
        Intent intent = new Intent(RegisterActivity.this, VerifyEmailActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("type", "register");
        startActivity(intent);
        finish();
    }

    @Override
    public void OnUserUpdated(String state) {

    }

    @Override
    public void OnCodeSent(String state) {

    }

    @Override
    public void OnApiNotFound(String message) {
        binding.progressBarLoading.setVisibility(View.GONE);
        showRegisterMsg(getString(R.string.phone_email_error), 0);
    }

    @Override
    public void OnPhoneVerification(String string) {

    }

    @Override
    public void OnCodeResend(String resendCodeResponse) {

    }

}