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
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.ProductPreviewActivity;
import app.wffr.viewmodels.GeneralProductViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> list;
    private Context context;
    private GeneralProductViewModel generalProductViewModel;

    public SearchProductsAdapter(Context context, List<Product> list, GeneralProductViewModel generalProductViewModel) {
        this.list = list;
        this.context = context;
        this.generalProductViewModel = generalProductViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Product product = list.get(i);
        String product_img_url = "https://wffr.app" + product.get_img();
        if (product.get_name().length() > 9){
            ((ViewHolder)viewHolder).txtName.setText(product.get_nameen().substring(0, 9) + "...");
        }else {
            ((ViewHolder)viewHolder).txtName.setText(product.get_nameen());
        }
        Glide.with(context).load(product_img_url).into(((ViewHolder)viewHolder).imgProduct);
        ((ViewHolder)viewHolder).cardView.setOnClickListener(v -> {
            if (LocalRepo.getUserData(context) == null){
                context.startActivity(new Intent(context, LoginActivity.class));
            }else {
                Intent intent = new Intent(context, ProductPreviewActivity.class);
                intent.putExtra("product", product);
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
        private ImageView imgProduct;
        private CircleImageView imgSeller;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtRat = itemView.findViewById(R.id.txt_rat);
            imgProduct = itemView.findViewById(R.id.img_product);
            imgSeller = itemView.findViewById(R.id.img_seller);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }


}