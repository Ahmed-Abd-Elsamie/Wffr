package app.wffr.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.models.NotificationItem;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.ProductPreviewActivity;
import app.wffr.utils.AppData;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationItem> list;
    private AppCompatActivity context;

    public NotificationsAdapter(AppCompatActivity context, List<NotificationItem> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NotificationItem notificationItem = list.get(i);
        String notif_img_url = "https://wffr.app" + notificationItem.getImg();

        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtTitle.setText(notificationItem.getTitleen());
            ((ViewHolder)viewHolder).txtDesc.setText(notificationItem.getDiscriptionen());
        }else {
            ((ViewHolder)viewHolder).txtTitle.setText(notificationItem.getTitlear());
            ((ViewHolder)viewHolder).txtDesc.setText(notificationItem.getDiscriptionar());
        }

        Glide.with(context).load(notif_img_url).into(((ViewHolder)viewHolder).img);
        ((ViewHolder)viewHolder).cardItem.setOnClickListener(v -> {
            if (notificationItem.getProduct_id() == 0){
                context.finish();
            }else {
                Intent intent = new Intent(context, ProductPreviewActivity.class);
                Product product = new Product();
                product.set_id(notificationItem.getProduct_id() + "");
                intent.putExtra("product", product);
                intent.putExtra("category", "category");
                context.startActivity(intent);
            }

        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private TextView txtDesc;
        private ImageView img;
        private CardView cardItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDesc = itemView.findViewById(R.id.txt_desc);
            img = itemView.findViewById(R.id.img_notification);
            cardItem = itemView.findViewById(R.id.card_item);

        }
    }
}