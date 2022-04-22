package app.wffr.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.wffr.R;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.ProductPreviewActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class PrevProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> list;
    private Context context;
    private String category;

    public PrevProductsAdapter(Context context, List<Product> list, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_prev_product, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Product product = list.get(i);
        String product_img_url = "https://wffr.app" + product.get_img();
        if(LocalRepo.getLanguage(context).equals("en")){
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

        if (product.getPrice_after() != 0){
            ((ViewHolder)viewHolder).prevPrice.setVisibility(View.VISIBLE);
            ((ViewHolder)viewHolder).txtDiscount.setText(Math.round(product.getPrice_after()) + "");
            ((ViewHolder)viewHolder).txtPrice.setText(Math.round(Float.parseFloat(product.get_price())) + "");
        }else {
            ((PrevProductsAdapter.ViewHolder)viewHolder).prevPrice.setVisibility(View.GONE);
        }

        if (product.get_price().equals("0.0") || product.get_price().equals("0")){
            ((PrevProductsAdapter.ViewHolder)viewHolder).prevPrice.setVisibility(View.GONE);
        }

        if (Float.parseFloat(product.getAssess()) == 0){
            ((PrevProductsAdapter.ViewHolder)viewHolder).txtRat.setText("0");
        }else {
            ((ViewHolder)viewHolder).txtRat.setText(product.getAssess());
        }

        if (isValidContextForGlide(context)){
            Glide.with(context).load(product_img_url).placeholder(R.drawable.playstore).into(((ViewHolder)viewHolder).imgProduct);
        }

        ((ViewHolder)viewHolder).cardView.setOnClickListener(v -> {
            if (LocalRepo.getUserData(context) == null){
                context.startActivity(new Intent(context, LoginActivity.class));
            }else {
                Intent intent = new Intent(context, ProductPreviewActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private TextView txtDiscount;
        private TextView txtRat;
        private TextView txtPrice;
        private RelativeLayout prevPrice;
        private ImageView imgProduct;
        private CircleImageView imgSeller;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtRat = itemView.findViewById(R.id.txt_rat);
            txtPrice = itemView.findViewById(R.id.txt_price);
            prevPrice = itemView.findViewById(R.id.prev_price);
            imgProduct = itemView.findViewById(R.id.img_product);
            imgSeller = itemView.findViewById(R.id.img_seller);
            cardView = itemView.findViewById(R.id.card_view);

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