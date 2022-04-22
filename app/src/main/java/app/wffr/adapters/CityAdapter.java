package app.wffr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.wffr.R;
import app.wffr.models.City;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<City> list;
    private Context context;

    public CityAdapter(Context context, List<City> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        City city = list.get(i);
        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtCityName.setText(city.getNameen());
        }else {
            ((ViewHolder)viewHolder).txtCityName.setText(city.getName());
        }

        ((ViewHolder)viewHolder).itemCard.setOnClickListener(v -> {
            LocalRepo.saveCity((Activity) context, city);
            ((Activity) context).finish();
            Toast.makeText(context, R.string.city_changed, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtCityName;
        private CardView itemCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCityName = itemView.findViewById(R.id.txt_city_name);
            itemCard = itemView.findViewById(R.id.item_card);
        }
    }
}