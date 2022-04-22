package app.wffr.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.wffr.BR;
import app.wffr.R;
import app.wffr.databinding.ItemProductFillBinding;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.ProductPreviewActivity;

public class ProductsAdapterFill extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> list;
    private Context context;
    private String category;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ProductsAdapterFill(Context context, List<Product> list, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType ) {

        if (viewType == VIEW_TYPE_ITEM) {
            ItemProductFillBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.getContext()),
                    R.layout.item_product_fill, viewGroup, false);
            return new ProductsAdapterFill.ViewHolder(binding);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
            return new LoadingViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof ViewHolder){
            Product product = list.get(i);
            String product_img_url = "https://wffr.app" + product.get_img();
            String shop_img_url = "https://wffr.app" + product.getShopImage();

            ((ProductsAdapterFill.ViewHolder)viewHolder).bind(product);

            if (isValidContextForGlide(context)){
                Glide.with(context).load(product_img_url).placeholder(R.drawable.playstore).into(((ProductsAdapterFill.ViewHolder)viewHolder).binding.imgProduct);
                Glide.with(context).load(shop_img_url).placeholder(R.drawable.playstore).into(((ProductsAdapterFill.ViewHolder)viewHolder).binding.imgSeller);
            }

            if (product.getPrice_after() != 0){
                ((ProductsAdapterFill.ViewHolder)viewHolder).binding.prevPrice.setVisibility(View.VISIBLE);
                ((ProductsAdapterFill.ViewHolder)viewHolder).binding.txtDiscount.setText(Math.round(product.getPrice_after()) + "");
                ((ProductsAdapterFill.ViewHolder)viewHolder).binding.txtPrice.setText(Math.round(Float.parseFloat(product.get_price())) + "");
            }else {
                ((ProductsAdapterFill.ViewHolder)viewHolder).binding.prevPrice.setVisibility(View.GONE);
            }

            if (product.get_price().equals("0.0") || product.get_price().equals("0")){
                ((ProductsAdapterFill.ViewHolder)viewHolder).binding.prevPrice.setVisibility(View.GONE);
            }

            if (Float.parseFloat(product.getAssess()) == 0){
                ((ViewHolder)viewHolder).binding.txtRat.setText("0");
            }

            ((ProductsAdapterFill.ViewHolder)viewHolder).binding.cardView.setOnClickListener(v -> {
                if (LocalRepo.getUserData(context) == null){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }else {
                    Intent intent = new Intent(context, ProductPreviewActivity.class);
                    intent.putExtra("product", product);
                    intent.putExtra("category", category);
                    context.startActivity(intent);
                }
            });
        }else if (viewHolder instanceof LoadingViewHolder) {

        }


    }

    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        /*private TextView txtName;
        private TextView txtDiscount;
        private TextView txtRat;
        private TextView txtSeen;
        private ImageView imgProduct;
        private CircleImageView imgSeller;
        private CardView cardView;*/
        private ItemProductFillBinding binding;

        public ViewHolder(ItemProductFillBinding binding) {
            super(binding.getRoot());
            /*txtName = itemView.findViewById(R.id.txt_name);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtRat = itemView.findViewById(R.id.txt_rat);
            txtSeen = itemView.findViewById(R.id.txt_seen);
            imgProduct = itemView.findViewById(R.id.img_product);
            imgSeller = itemView.findViewById(R.id.img_seller);
            cardView = itemView.findViewById(R.id.card_view);*/
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.setVariable(BR.product, product);
            binding.executePendingBindings();
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }


}