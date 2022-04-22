package app.wffr.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.wffr.R;
import app.wffr.listeners.ProductsListener;
import app.wffr.models.Category;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.fragments.SearchFragment;
import app.wffr.utils.AppData;
import app.wffr.utils.Constants;
import app.wffr.viewmodels.GeneralProductViewModel;

public class CategoriesRadioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> list;
    private Context context;
    private Category currentItem;
    private GeneralProductViewModel generalProductViewModel;
    private ProductsListener productsListener;

    public CategoriesRadioAdapter(Context context, List<Category> list,
                                  GeneralProductViewModel generalProductViewModel, ProductsListener productsListener) {
        this.list = list;
        this.context = context;
        this.generalProductViewModel = generalProductViewModel;
        this.productsListener = productsListener;
        currentItem = list.get(0);
        resetSelection();
    }

    private void resetSelection() {
        if (list.size() < 2){
            return;
        }
        for (int i = 1; i < list.size(); i++){
            list.get(i).setChecked(false);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_radio_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Category category = list.get(i);

        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).radioButton.setText(category.getName_en());
        }else {
            ((ViewHolder)viewHolder).radioButton.setText(category.getName());
        }

        ((ViewHolder)viewHolder).radioButton.setChecked(category.isChecked());

        ((ViewHolder)viewHolder).radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        ((ViewHolder)viewHolder).radioButton.setOnClickListener(v -> {

            if (currentItem != null && currentItem != category){
                currentItem.setChecked(false);
                category.setChecked(true);
                notifyChange();
            }

            currentItem = list.get(i);

            if (i == 0){
                SearchFragment.categoryName = "0";
            }else {
                SearchFragment.categoryName = category.getId();
            }

            SearchFragment.categoryChangeTotal = true;
            SearchFragment.categoryChangeFeat = true;

            // reset pages counts
            SearchFragment.total_bill_page = 0;
            SearchFragment.products_page = 0;

            if (TextUtils.isEmpty(SearchFragment.shopName)){
                generalProductViewModel.loadGeneralProducts(
                        SearchFragment.categoryName,
                        Math.round(SearchFragment.rangeSlider.getValues().get(0)) + "",
                        Math.round(SearchFragment.rangeSlider.getValues().get(1)) + "",
                        "",
                        Constants.REFRESH,
                        productsListener,
                        SearchFragment.total_bill_page,
                        10,
                        SearchFragment.products_page
                );
            }else {
                generalProductViewModel.loadGeneralProducts(
                        SearchFragment.categoryName,
                        Math.round(SearchFragment.rangeSlider.getValues().get(0)) + "",
                        Math.round(SearchFragment.rangeSlider.getValues().get(1)) + "",
                        SearchFragment.shopName,
                        Constants.REFRESH,
                        productsListener,
                        0,
                        10,
                        0
                );
            }



        });


    }

    private void notifyChange() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_category);
        }

    }


}