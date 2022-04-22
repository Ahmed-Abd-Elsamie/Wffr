package app.wffr.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import app.wffr.viewmodels.ShopsViewModel;

public class ShopRatingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*AlertDialog builder = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_rating, null);
        builder.setView(view);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RatingBar ratingBar = view.findViewById(R.id.rat_bar);
        Button btnSend = view.findViewById(R.id.btn_send);

        String shopId = intent.getStringExtra("shopId");
        String clientId = intent.getStringExtra("clientId");

        btnSend.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            submitRat(context, builder, clientId, shopId, rating);
        });

        builder.show();*/
    }

    private void submitRat(Context context, AlertDialog builder, String clientId, String shopId, float rating) {
        builder.dismiss();
        ShopsViewModel shopsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ShopsViewModel.class);
        shopsViewModel.init((Activity) context);
        shopsViewModel.ratShop((int)rating+"", clientId, shopId);
        shopsViewModel.getRating().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Created")){
                    //Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}