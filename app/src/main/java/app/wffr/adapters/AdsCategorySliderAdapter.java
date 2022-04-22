package app.wffr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import app.wffr.R;
import app.wffr.models.AdCategoryModel;

public class AdsCategorySliderAdapter extends SliderViewAdapter {

    private List<AdCategoryModel> list;
    private Context context;

    public AdsCategorySliderAdapter(Context context, List<AdCategoryModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public AdsCategorySliderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads, parent, false);
        AdsCategorySliderAdapter.ViewHolder viewHolder = new AdsCategorySliderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SliderViewAdapter.ViewHolder viewHolder, int position) {
        AdCategoryModel adModel = list.get(position);
        String ad_img_url = "https://wffr.app" + adModel.getImage();
        Glide.with(context).load(ad_img_url).into(((AdsCategorySliderAdapter.ViewHolder)viewHolder).imgAd);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private class ViewHolder extends SliderViewAdapter.ViewHolder{
        private ImageView imgAd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAd = itemView.findViewById(R.id.img_ad);
        }
    }


}