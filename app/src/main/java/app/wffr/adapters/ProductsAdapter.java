package app.wffr.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.wffr.BR;
import app.wffr.R;
import app.wffr.databinding.ItemProductBinding;
import app.wffr.databinding.ItemProductFillBinding;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.ProductPreviewActivity;
import app.wffr.utils.AppData;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> list;
    private Context context;
    private String category;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ProductsAdapter(Context context, List<Product> list, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            ItemProductBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.getContext()),
                    R.layout.item_product, viewGroup, false);
            return new ProductsAdapter.ViewHolder(binding);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading_sized, viewGroup, false);
            return new ProductsAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof ProductsAdapter.ViewHolder){
            Product product = list.get(i);
            String product_img_url = "https://wffr.app" + product.get_img();
            String shop_img_url = "https://wffr.app" + product.getShopImage();
        /*if(LocalRepo.getLanguage(context).equals("en")){
            if (product.get_nameen().length() > 7){
                ((ViewHolder)viewHolder).txtName.setText(product.get_nameen().substring(0, 7) + "..");
            }else {
                ((ViewHolder)viewHolder).txtName.setText(product.get_nameen());
            }

        }else {
            if (product.get_name().length() > 7){
                ((ViewHolder)viewHolder).txtName.setText(product.get_name().substring(0, 7) + "..");
            }else {
                ((ViewHolder)viewHolder).txtName.setText(product.get_name());
            }

        }

        ((ViewHolder)viewHolder).txtDiscount.setText(product.get_disc() + "%");
        ((ViewHolder)viewHolder).txtSeen.setText(product.getWatch());
        ((ViewHolder)viewHolder).txtRat.setText((Float.parseFloat(product.getAssess())) + "");
*/


            ((ViewHolder)viewHolder).bind(product);


            if (isValidContextForGlide(context)){
                Glide.with(context).load(product_img_url).placeholder(R.drawable.playstore).into(((ViewHolder)viewHolder).binding.imgProduct);
                Glide.with(context).load(shop_img_url).placeholder(R.drawable.playstore).into(((ViewHolder)viewHolder).binding.imgSeller);
            }

            //Picasso.get().load(product_img_url).into(((ViewHolder)viewHolder).imgProduct);
            //Picasso.get().load(shop_img_url).into(((ViewHolder)viewHolder).imgSeller);

            if (product.getPrice_after() != 0){
                ((ProductsAdapter.ViewHolder)viewHolder).binding.prevPrice.setVisibility(View.VISIBLE);
                ((ProductsAdapter.ViewHolder)viewHolder).binding.txtDiscount.setText(Math.round(product.getPrice_after()) + "");
                ((ProductsAdapter.ViewHolder)viewHolder).binding.txtPrice.setText(Math.round(Float.parseFloat(product.get_price())) + "");
            }else {
                ((ProductsAdapter.ViewHolder)viewHolder).binding.prevPrice.setVisibility(View.GONE);
            }

            if (product.get_price().equals("0.0") || product.get_price().equals("0")){
                ((ProductsAdapter.ViewHolder)viewHolder).binding.prevPrice.setVisibility(View.GONE);
            }

            if (Float.parseFloat(product.getAssess()) == 0){
                ((ProductsAdapter.ViewHolder)viewHolder).binding.txtRat.setText("0");
            }

            ((ViewHolder)viewHolder).binding.cardView.setOnClickListener(v -> {
                if (LocalRepo.getUserData(context) == null){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }else {
                    Intent intent = new Intent(context, ProductPreviewActivity.class);
                    intent.putExtra("product", product);
                    intent.putExtra("category", category);
                    context.startActivity(intent);
                }
            });
        }else if (viewHolder instanceof ProductsAdapter.LoadingViewHolder) {

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
        private ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding) {
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