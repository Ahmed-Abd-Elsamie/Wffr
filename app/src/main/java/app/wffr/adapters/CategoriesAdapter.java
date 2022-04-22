package app.wffr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.wffr.R;
import app.wffr.models.Category;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.CategoriesViewActivity;
import app.wffr.utils.AppData;

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> list;
    private Context context;
    private Integer  imagesList [] = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3,
            R.drawable.slide4,
            R.drawable.slide5,
            R.drawable.slide7,
            R.drawable.slide6,
            R.drawable.slide8
    };

    public CategoriesAdapter(Context context, List<Category> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Category category = list.get(i);
        String category_img_url = "https://wffr.app" + category.getImg();

        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtTitle.setText(category.getName_en());
        }else {
            ((ViewHolder)viewHolder).txtTitle.setText(category.getName());
        }

        Glide.with(context).load(category_img_url).into(((ViewHolder)viewHolder).imgDesc);
        //((ViewHolder)viewHolder).imgDesc.setImageResource(imagesList[i]);

        ((ViewHolder)viewHolder).categoryItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoriesViewActivity.class);
            intent.putExtra("category", category);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private ImageView imgDesc;
        private RelativeLayout categoryItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_category_title);
            imgDesc = itemView.findViewById(R.id.img_category);
            categoryItem = itemView.findViewById(R.id.category_item);
        }
    }
}