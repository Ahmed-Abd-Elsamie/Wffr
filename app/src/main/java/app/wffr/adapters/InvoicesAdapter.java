package app.wffr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.wffr.R;
import app.wffr.models.Invoice;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;

public class InvoicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Invoice> list;
    private Context context;

    public InvoicesAdapter(Context context, List<Invoice> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Invoice order = list.get(i);

        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtName.setText(order.getProducten());
        }else {
            ((ViewHolder)viewHolder).txtName.setText(order.getProductar());
        }


        ((ViewHolder)viewHolder).txtTotal.setText(order.getTotalInvois());
        ((ViewHolder)viewHolder).txtBefore.setText(order.getTotal());
        ((ViewHolder)viewHolder).txtDiscount.setText(order.getDisc());
        if (order.getDisc().equals("0") || order.getDisc().equals("0.0")){
            ((ViewHolder)viewHolder).txtDiscount.setVisibility(View.GONE);
            ((ViewHolder)viewHolder).txtDescTitle.setVisibility(View.GONE);
        }else {
            ((ViewHolder)viewHolder).txtDiscount.setText(order.getDisc() + " % ");
            ((ViewHolder)viewHolder).txtDiscount.setVisibility(View.VISIBLE);
        }

        ((ViewHolder)viewHolder).txtDate.setText(order.getDate().substring(0, 10));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private TextView txtBefore;
        private TextView txtDiscount;
        private TextView txtDate;
        private TextView txtTotal;
        private TextView txtDescTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtBefore = itemView.findViewById(R.id.txt_before);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtTotal = itemView.findViewById(R.id.txt_price);
            txtDescTitle = itemView.findViewById(R.id.disc_title);

        }
    }
}