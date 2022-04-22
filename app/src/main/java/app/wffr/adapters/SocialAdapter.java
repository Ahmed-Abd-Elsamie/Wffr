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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.wffr.R;
import app.wffr.models.SocialModel;
import app.wffr.ui.activities.AppWebBrowser;

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SocialModel> list;
    private Context context;

    public SocialAdapter(Context context, List<SocialModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_social, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        SocialModel socialModel = list.get(i);
        ((ViewHolder)viewHolder).txtName.setText(socialModel.getName());
        String social_img_url = "https://wffr.app" + socialModel.getImg();
        Glide.with(context).load(social_img_url).into(((ViewHolder)viewHolder).img);

        ((ViewHolder)viewHolder).itemCard.setOnClickListener(v -> {
            if (socialModel.getUrl() == null || socialModel.getUrl().equals("")){

            }else {
                Uri uri = Uri.parse(socialModel.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                /*Intent intent = new Intent(context, AppWebBrowser.class);
                intent.putExtra("url", socialModel.getUrl());
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private ImageView img;
        private CardView itemCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_social);
            img = itemView.findViewById(R.id.img_social);
            itemCard = itemView.findViewById(R.id.item_card);
        }
    }
}