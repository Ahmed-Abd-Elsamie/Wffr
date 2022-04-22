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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityEditProfileBinding;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.utils;
import app.wffr.viewmodels.AccountViewModel;

public class EditProfileActivity extends WffrBaseActivity implements LoginRegisterListener {

    private ActivityEditProfileBinding binding;
    private AccountViewModel accountViewModel;
    private User currentUser;
    private int GALLERY_REQUEST = 100;
    private Uri imgUri;
    private Bitmap bitmap;
    private String img_url = "";
    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        currentUser = LocalRepo.getUserData(this);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.init(this, this);

        fillUserData();

        binding.imgProfile.setOnClickListener(v -> {
            viewGallery();
        });

        binding.btnSave.setOnClickListener(v -> {
            validateUserData();
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
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

    private void saveUserDataLocal(User user) {
        Random random = new Random(100);
        int refreshId = random.nextInt();
        if (bitmap == null){
            user.setImg(currentUser.getImg());
        }else {
            user.setImg("/ClientImgs/" + currentUser.getId() + "/" + currentUser.getId() +".png?id=" + refreshId);
        }
        LocalRepo.saveUserData(this, user);
        MainActivity.user = user;
    }

    private void fillUserData() {
        binding.txtName.setText(currentUser.getName());
        binding.txtEmail.setText(currentUser.getEmail());
        binding.txtPass.setText(currentUser.getPassword());
        binding.txtPhone.setText(currentUser.getPhone());
        String user_img_url = "https://wffr.app" + currentUser.getImg();
        Glide.with(this).load(user_img_url).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(binding.imgProfile);
    }

    private void validateUserData() {

        String name = binding.txtName.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, getString(R.string.error_enter_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidFirstChar(name)){
            Toast.makeText(this, R.string.first_char_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidWord(name)){
            Toast.makeText(this, getString(R.string.error_enter_valid_name), Toast.LENGTH_SHORT).show();
            return;
        }

        newUser = new User(
                currentUser.getId(),
                name,
                currentUser.getEmail(),
                currentUser.getPassword(),
                currentUser.getPhone(),
                "",
                currentUser.getGender(),
                currentUser.getCreationDate()
        );

        if (bitmap == null){
            accountViewModel.updateUser(newUser);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            String userImg = imgToString();
            newUser.setImg(userImg);

            accountViewModel.updateUser(newUser);
            binding.progressBar.setVisibility(View.VISIBLE);
        }



    }

    private void viewGallery() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i , GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST){
            imgUri = data.getData();
            binding.imgProfile.setImageURI(imgUri);
            img_url = imgUri.toString();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            }catch (Exception e){
                System.out.println("UPLOAD ERRORRRRR : " + e.getMessage());
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

        btnOk.setText(getString(R.string.ok_dialog));

        btnOk.setOnClickListener(v -> {
            builder.dismiss();
        });

        builder.show();
    }


    @Override
    public void OnFailure(String message) {
        binding.progressBar.setVisibility(View.GONE);
        showRegisterMsg("Not Updated", 0);
    }

    @Override
    public void OnUserRegister(User user) {

    }

    @Override
    public void OnUserUpdated(String state) {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(EditProfileActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
        saveUserDataLocal(newUser);
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