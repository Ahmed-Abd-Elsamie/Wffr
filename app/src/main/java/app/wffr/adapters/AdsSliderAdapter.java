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
import app.wffr.models.AdModel;

public class AdsSliderAdapter extends SliderViewAdapter {

    private List<AdModel> list;
    private Context context;

    public AdsSliderAdapter(Context context, List<AdModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SliderViewAdapter.ViewHolder viewHolder, int position) {
        AdModel adModel = list.get(position);
        String ad_img_url = "https://wffr.app" + adModel.getImg();
        Glide.with(context).load(ad_img_url).into(((ViewHolder)viewHolder).imgAd);
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