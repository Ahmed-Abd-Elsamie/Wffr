package app.wffr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.wffr.R;
import app.wffr.models.Shop;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.ShopProductsActivity;
import app.wffr.utils.AppData;

public class ShopsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Shop> list;
    private Context context;
    private String category;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 2;

    public ShopsAdapter(Context context, List<Shop> list, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shop, viewGroup, false);
        //ViewHolder viewHolder = new ViewHolder(view);
        //return viewHolder;
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shop, viewGroup, false);
            ShopsAdapter.ViewHolder viewHolder = new ShopsAdapter.ViewHolder(view);
            return viewHolder;
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
            ShopsAdapter.ViewHolder viewHolder = new ShopsAdapter.ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == VIEW_TYPE_ITEM){
            Shop shop = list.get(i);
            String product_img_url = "https://wffr.app" + shop.getImg();
            if(LocalRepo.getLanguage(context).equals("en")){
                ((ViewHolder)viewHolder).txtName.setText(shop.getNameen());
            }else {
                ((ViewHolder)viewHolder).txtName.setText(shop.getName());
            }
            Glide.with(context).load(product_img_url).into(((ViewHolder)viewHolder).imgShop);

            ((ViewHolder)viewHolder).cardView.setOnClickListener(v -> {
                if (LocalRepo.getUserData(context) == null){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }else {
                    Intent intent = new Intent(context, ShopProductsActivity.class);
                    intent.putExtra("shop", shop);
                    intent.putExtra("category", category);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null){
            return VIEW_TYPE_LOADING;
        }else{
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private ImageView imgShop;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            imgShop = itemView.findViewById(R.id.img_shop);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}