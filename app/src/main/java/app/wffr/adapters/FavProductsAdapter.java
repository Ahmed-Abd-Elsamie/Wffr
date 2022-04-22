package app.wffr.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.listeners.FavoriteListener;
import app.wffr.models.FavoriteRequest;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.ProductPreviewActivity;
import app.wffr.utils.AppData;
import app.wffr.utils.utils;
import app.wffr.viewmodels.FavoritesViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> list;
    private Context context;
    private FavoritesViewModel favoritesViewModel;
    private FavoriteListener favoriteListener;

    public FavProductsAdapter(Context context, List<Product> list, FavoritesViewModel favoritesViewModel, FavoriteListener favoriteListener) {
        this.list = list;
        this.context = context;
        this.favoriteListener = favoriteListener;
        this.favoritesViewModel = favoritesViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fav_product, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Product product = list.get(i);
        String product_img_url = "https://wffr.app" + product.get_img();
        String shop_img_url = "https://wffr.app" + product.getShopImage();

        if(LocalRepo.getLanguage(context).equals("en")){
            if (product.get_nameen().length() > 7){
                ((ViewHolder)viewHolder).txtName.setText(product.get_nameen());
            }else {
                ((ViewHolder)viewHolder).txtName.setText(product.get_nameen());
            }

        }else {
            if (product.get_name().length() > 7){
                ((ViewHolder)viewHolder).txtName.setText(product.get_name());
            }else {
                ((ViewHolder)viewHolder).txtName.setText(product.get_name());
            }

        }

        ((ViewHolder)viewHolder).txtDiscount.setText(product.get_disc() + "%");

        if (product.getPrice_after() != 0){
            ((FavProductsAdapter.ViewHolder)viewHolder).prevPrice.setVisibility(View.VISIBLE);
            ((FavProductsAdapter.ViewHolder)viewHolder).txtDiscount.setText(Math.round(product.getPrice_after()) + "");
            ((FavProductsAdapter.ViewHolder)viewHolder).txtPrice.setText(Math.round(Float.parseFloat(product.get_price())) + "");
        }else {
            ((FavProductsAdapter.ViewHolder)viewHolder).prevPrice.setVisibility(View.GONE);
        }

        if (product.get_price().equals("0.0") || product.get_price().equals("0")){
            ((FavProductsAdapter.ViewHolder)viewHolder).prevPrice.setVisibility(View.GONE);
        }

        if (Float.parseFloat(product.getAssess()) == 0){
            ((ViewHolder)viewHolder).txtRat.setText("0");
        }else {
            ((ViewHolder)viewHolder).txtRat.setText(product.getAssess());
        }

        ((ViewHolder)viewHolder).txtSeen.setText(product.getWatch());

        if (isValidContextForGlide(context)){
            Glide.with(context).load(product_img_url).placeholder(R.drawable.playstore).into(((ViewHolder)viewHolder).imgProduct);
            Glide.with(context).load(shop_img_url).placeholder(R.drawable.playstore).into(((ViewHolder)viewHolder).imgSeller);
        }
        //Picasso.get().load(product_img_url).into(((ViewHolder)viewHolder).imgProduct);
        //Picasso.get().load(shop_img_url).into(((ViewHolder)viewHolder).imgSeller);

        ((ViewHolder)viewHolder).cardView.setOnClickListener(v -> {
            if (LocalRepo.getUserData(context) == null){
                context.startActivity(new Intent(context, LoginActivity.class));
            }else {
                Intent intent = new Intent(context, ProductPreviewActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("category", "");
                context.startActivity(intent);
            }
        });

        ((ViewHolder)viewHolder).btnFav.setOnClickListener(v -> {
            if (!utils.internetIsConnected()){
                return;
            }
            /*ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
*/
            FavoriteRequest favoriteRequest = new FavoriteRequest(
                    "0",
                    product.get_id(),
                    LocalRepo.getUserData(context).getId()
            );

            favoritesViewModel.addRemoveFavorites(favoriteRequest, product);
            favoriteListener.OnLoading(true);

            /*favoritesViewModel.addToFavorites(favoriteRequest, new FavoriteListener() {
                @Override
                public void onComplete(@NonNull String message) {
                    progressDialog.hide();
                    if (message.equals("added")){
                        Toast.makeText(context, context.getString(R.string.fav_added), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, context.getString(R.string.fav_removed), Toast.LENGTH_SHORT).show();
                        favoritesViewModel.loadFavorites(MainActivity.user.getId());
                    }
                }
            });*/
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
        private TextView txtSeen;
        private ImageView imgProduct;
        private ImageView btnFav;
        private CircleImageView imgSeller;
        private CardView cardView;
        private TextView txtPrice;
        private RelativeLayout prevPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtRat = itemView.findViewById(R.id.txt_rat);
            txtSeen = itemView.findViewById(R.id.txt_seen);
            imgProduct = itemView.findViewById(R.id.img_product);
            btnFav = itemView.findViewById(R.id.btn_remove_fav);
            imgSeller = itemView.findViewById(R.id.img_seller);
            cardView = itemView.findViewById(R.id.card_view);
            txtPrice = itemView.findViewById(R.id.txt_price);
            prevPrice = itemView.findViewById(R.id.prev_price);
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