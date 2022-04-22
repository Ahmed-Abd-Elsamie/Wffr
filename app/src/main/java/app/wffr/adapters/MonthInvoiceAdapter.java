package app.wffr.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.wffr.R;
import app.wffr.models.MonthInvoice;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;

public class MonthInvoiceAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MonthInvoice> list;
    private Context context;

    public MonthInvoiceAdapter(Context context, List<MonthInvoice> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_month_invoice, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MonthInvoice invoice = list.get(i);

        if(LocalRepo.getLanguage(context).equals("en")){
            ((ViewHolder)viewHolder).txtName.setText(invoice.getShopNameen());
        }else {
            ((ViewHolder)viewHolder).txtName.setText(invoice.getShopNamear());
        }


        ((ViewHolder)viewHolder).txtTotal.setText(invoice.getTotalfinal());
        ((ViewHolder)viewHolder).txtBefore.setText(invoice.getTotal());
        if (invoice.getDisc().equals("0") || invoice.getDisc().equals("0.0")){
            ((ViewHolder)viewHolder).txtDiscount.setVisibility(View.GONE);
            ((ViewHolder)viewHolder).txtDescTitle.setVisibility(View.GONE);
        }else {
            ((ViewHolder)viewHolder).txtDiscount.setText(invoice.getDisc() + " % ");
            ((ViewHolder)viewHolder).txtDiscount.setVisibility(View.VISIBLE);
        }

        Resources res = context.getResources(); //assuming in an activity for example, otherwise you can provide a context.
        String[] shoppingItems = res.getStringArray(R.array.months);
        String month = shoppingItems[Integer.parseInt(invoice.getMounth()) - 1];
        ((ViewHolder)viewHolder).txtDate.setText(month);
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