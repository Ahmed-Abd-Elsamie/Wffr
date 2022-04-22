package app.wffr.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityAddShopBinding;
import app.wffr.listeners.MarketRequestListener;
import app.wffr.models.MarketRequest;
import app.wffr.utils.utils;
import app.wffr.viewmodels.MarketRequestViewModel;

public class AddShopActivity extends WffrBaseActivity {

    private ActivityAddShopBinding binding;
    private MarketRequestViewModel marketRequestViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_shop);

        marketRequestViewModel = new ViewModelProvider(this).get(MarketRequestViewModel.class);
        marketRequestViewModel.init(new MarketRequestListener() {
            @Override
            public void onComplete(@NonNull String message, boolean isSuccess) {
                binding.progressBar.setVisibility(View.GONE);
                //Toast.makeText(AddShopActivity.this, message, Toast.LENGTH_LONG).show();
                if (isSuccess){
                    //Toast.makeText(AddShopActivity.this, getString(R.string.request_shop_success), Toast.LENGTH_LONG).show();
                    showAlertDialog();
                }else {
                    Toast.makeText(AddShopActivity.this, getString(R.string.request_shop_error), Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.btnSend.setOnClickListener(v -> {
            validateData();
        });


        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtName.requestFocus();
        binding.txtEmail.clearFocus();
        binding.txtPhone.clearFocus();
        binding.txtNotes.clearFocus();
        InputMethodManager in = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.txtName.getWindowToken(), 0);
        in.hideSoftInputFromWindow(binding.txtEmail.getWindowToken(), 0);
        in.hideSoftInputFromWindow(binding.txtPhone.getWindowToken(), 0);
        in.hideSoftInputFromWindow(binding.txtNotes.getWindowToken(), 0);


    }

    private void validateData() {
        String name = binding.txtName.getText().toString();
        String email = binding.txtEmail.getText().toString();
        String phone = binding.txtPhone.getText().toString();
        String notes = binding.txtNotes.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, getString(R.string.shop_name_empty), Toast.LENGTH_SHORT).show();
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

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, getString(R.string.error_enter_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isEmailValid(email)){
            Toast.makeText(this, getString(R.string.error_enter_valid_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, getString(R.string.error_enter_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidMobile(phone)){
            Toast.makeText(this, getString(R.string.error_enter_valid_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(notes)){
            Toast.makeText(this, getString(R.string.note_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidFirstChar(notes)){
            Toast.makeText(this, R.string.first_char_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidWord(notes)){
            Toast.makeText(this, getString(R.string.note_error), Toast.LENGTH_SHORT).show();
            return;
        }

        MarketRequest marketRequest = new MarketRequest(
                "0",
                phone,
                notes,
                email,
                name
        );

        marketRequestViewModel.addMarket(marketRequest);
        binding.progressBar.setVisibility(View.VISIBLE);
    }


    private void showAlertDialog() {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_message, null);
        builder.setView(view);
        builder.setCancelable(false);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = view.findViewById(R.id.btn_done);
        TextView txtMsg = view.findViewById(R.id.txt_message);

        txtMsg.setText(getString(R.string.add_shop_alert));

        btnOk.setOnClickListener(v -> {
            builder.dismiss();
            finish();
        });

        builder.show();
    }


}