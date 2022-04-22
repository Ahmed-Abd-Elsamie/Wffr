package app.wffr.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import app.wffr.R;
import app.wffr.models.Branch;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.ProductPreviewActivity;

public class BranchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Branch> list;
    private Context context;

    public BranchesAdapter(Context context, List<Branch> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_branch, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Branch branch = list.get(i);
        ((ViewHolder)viewHolder).txtLoc.setText(branch.getAddressen());
        ((ViewHolder)viewHolder).txtPhone.setText(branch.getPhone()[0]);

        if (branch.getPhone().length > 1){
            ((ViewHolder)viewHolder).btnMore.setVisibility(View.VISIBLE);
            ((ViewHolder)viewHolder).btnMore.setOnClickListener(v -> {
                showAllNumbersDialog(branch.getPhone());
            });
        }

        ((ViewHolder)viewHolder).txtPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + branch.getPhone()[0]));
            context.startActivity(intent);
        });

        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtLoc.setText(branch.getAddressen());
        }else {
            ((ViewHolder)viewHolder).txtLoc.setText(branch.getAddress());
        }

        ((ViewHolder)viewHolder).txtLoc.setOnClickListener(v -> {
            String[] loc = branch.getMap().split(",", 2);
            LatLng mapLoc = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));

            if(LocalRepo.getLanguage(context).equals("en")){
                ProductPreviewActivity.mMap.addMarker(new MarkerOptions().position(mapLoc).title(branch.getAddressen()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wffr_location)));
            }else {
                ProductPreviewActivity.mMap.addMarker(new MarkerOptions().position(mapLoc).title(branch.getAddress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wffr_location)));
            }

            ProductPreviewActivity.mMap.moveCamera(CameraUpdateFactory.newLatLng(mapLoc));
            ProductPreviewActivity.mMap.setMinZoomPreference(13);
            ProductPreviewActivity.nestedScrollView.fullScroll(View.FOCUS_DOWN);


        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtLoc;
        private TextView txtPhone;
        private TextView btnMore;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLoc = itemView.findViewById(R.id.txt_location);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            btnMore = itemView.findViewById(R.id.btn_more);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }


    private void showAllNumbersDialog(String[] phones) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(phones, (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phones[which]));
            context.startActivity(intent);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}