package app.wffr.ui.activities;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.MonthInvoiceAdapter;
import app.wffr.databinding.ActivityReportsBinding;
import app.wffr.models.MonthInvoice;
import app.wffr.models.ShopInvoiceModel;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.MonthYearPickerDialog;
import app.wffr.viewmodels.PrevOrdersViewModel;

public class ReportsActivity extends WffrBaseActivity {

    private ActivityReportsBinding binding;
    private PrevOrdersViewModel prevOrdersViewModel;
    private MonthInvoiceAdapter adapter;
    private List<ShopInvoiceModel> prevShopsList;
    private boolean isShops = false;
    private int from_month = 0;
    private int to_month = 0;

    private int from_year = 0;
    private int to_year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reports);


        initRecyclerView();

        prevOrdersViewModel = new ViewModelProvider(this).get(PrevOrdersViewModel.class);
        prevOrdersViewModel.init(this);
        prevOrdersViewModel.loadPrevOrdersByMonth("", LocalRepo.getUserData(this).getId(), "2020-02-20", "2020-02-20");

        prevOrdersViewModel.getMonthReports().observe(this, orders -> {
            if (orders == null || orders.size() == 0){
                binding.imgNoData.setVisibility(View.VISIBLE);
                binding.txtNoData.setVisibility(View.VISIBLE);
                binding.recyclerReports.setAdapter(null);
                return;
            }
            binding.imgNoData.setVisibility(View.GONE);
            binding.txtNoData.setVisibility(View.GONE);
            adapter = new MonthInvoiceAdapter(ReportsActivity.this, orders);
            binding.recyclerReports.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        prevOrdersViewModel.getPrevShops().observe(this, new Observer<List<ShopInvoiceModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<ShopInvoiceModel> shops) {
                if (shops != null){
                    if (shops.size() > 0){
                        if (!isShops){
                            prevShopsList = shops;
                            List<String> shopsNames = new ArrayList<>();
                            if (LocalRepo.getLanguage(ReportsActivity.this).equals("en")){
                                shopsNames.add("Select Shop");
                                for (ShopInvoiceModel invoiceModel : shops){
                                    shopsNames.add(invoiceModel.getNameen());
                                }
                            }else {
                                shopsNames.add("اختر المحل");
                                for (ShopInvoiceModel invoiceModel : shops){
                                    shopsNames.add(invoiceModel.getNamear());
                                }
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReportsActivity.this, android.R.layout.simple_spinner_item, shopsNames);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerShops.setAdapter(arrayAdapter);
                            isShops = true;
                        }
                    }
                }
                binding.progressBarShops.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        prevOrdersViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBar);
                    showProgressBar(binding.progressBarShops);
                }else {
                    hideProgressBar(binding.progressBar);
                    hideProgressBar(binding.progressBarShops);
                }
            }
        });

        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        /*binding.spinnerFromMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int from_month = binding.spinnerFromMonths.getSelectedItemPosition() + 1;
                int to_month = binding.spinnerToMonth.getSelectedItemPosition() + 1;
                if (isShops){
                    String shopId = prevShopsList.get(binding.spinnerShops.getSelectedItemPosition()).getId();
                    prevOrdersViewModel.loadPrevOrdersByMonth(shopId, MainActivity.user.getId(), from_month+"", to_month+"");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*binding.spinnerToMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int from_month = binding.spinnerFromMonths.getSelectedItemPosition() + 1;
                int to_month = binding.spinnerToMonth.getSelectedItemPosition() + 1;
                if (isShops){
                    String shopId = prevShopsList.get(binding.spinnerShops.getSelectedItemPosition()).getId();
                    prevOrdersViewModel.loadPrevOrdersByMonth(shopId, MainActivity.user.getId(), from_month+"", to_month+"");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        binding.spinnerShops.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (from_month == 0 || from_year == 0 || to_month == 0 || to_year == 0 || binding.spinnerShops.getSelectedItemPosition() == 0){

                }else {
                    if (isShops){
                        String shopId = prevShopsList.get(binding.spinnerShops.getSelectedItemPosition()-1).getId();
                        String fmo = from_month<=9 ? "0" + from_month : from_month + "";
                        String tmo = to_month<=9 ? "0" + to_month : to_month + "";
                        prevOrdersViewModel.loadPrevOrdersByMonth(
                                shopId,
                                MainActivity.user.getId(),
                                from_year + "-" + fmo + "-" + "01",
                                to_year + "-" + tmo + "-" + "01");
                    }
                }

                /*int from_month = binding.spinnerFromMonths.getSelectedItemPosition() + 1;
                int to_month = binding.spinnerToMonth.getSelectedItemPosition() + 1;*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnFrom.setOnClickListener(v -> {
            showYearDialog(0);
        });

        binding.btnTo.setOnClickListener(v -> {
            showYearDialog(1);
        });


    }

    private void showYearDialog(int type) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (type == 0){ // from
                    from_month = month;
                    from_year = year;
                    binding.btnFrom.setText(year + "-" + month);
                }else { // to
                    to_month = month;
                    to_year = year;
                    binding.btnTo.setText(year + "-" + month);
                }

                if (from_month == 0 || from_year == 0 || to_month == 0 || to_year == 0 || binding.spinnerShops.getSelectedItemPosition() == 0){

                }else {
                    //Toast.makeText(ReportsActivity.this, from_year + "-" + from_month + "-" + "01", Toast.LENGTH_SHORT).show();
                    if (isShops){
                        String shopId = prevShopsList.get(binding.spinnerShops.getSelectedItemPosition() - 1).getId();
                        String fmo = from_month<=9 ? "0" + from_month : from_month + "";
                        String tmo = to_month<=9 ? "0" + to_month : to_month + "";
                        prevOrdersViewModel.loadPrevOrdersByMonth(
                                shopId,
                                MainActivity.user.getId(),
                                from_year + "-" + fmo + "-" + "01",
                                to_year + "-" + tmo + "-" + "01");

                    }
                }

            }
        };

        MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
        newFragment.setListener(listener);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }


    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerReports.setLayoutManager(layoutManager);
        // set animations
        LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_up);
        binding.recyclerReports.setLayoutAnimation(categoriesAnim);
        binding.recyclerReports.scheduleLayoutAnimation();
    }



}