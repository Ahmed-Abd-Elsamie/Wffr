package app.wffr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.wffr.R;
import app.wffr.models.CategoryRatio;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;

public class CategoryRatioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryRatio> list;
    private Context context;

    public CategoryRatioAdapter(Context context, List<CategoryRatio> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_ratio, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CategoryRatio category = list.get(i);
        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtTitle.setText(category.getDepNameen());
        }else {
            ((ViewHolder)viewHolder).txtTitle.setText(category.getDepNamear());
        }
        ((ViewHolder)viewHolder).txtProgress.setText(category.getRatio());
        ((ViewHolder)viewHolder).progressBar.setProgress((int) Float.parseFloat(category.getRatio()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private TextView txtProgress;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_category_title);
            txtProgress = itemView.findViewById(R.id.txt_progress);
            progressBar = itemView.findViewById(R.id.progress_category);
        }
    }
}