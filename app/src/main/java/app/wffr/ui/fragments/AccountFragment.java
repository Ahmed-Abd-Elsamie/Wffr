package app.wffr.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseFragment;
import app.wffr.adapters.CategoryRatioAdapter;
import app.wffr.databinding.AccountFragmentBinding;
import app.wffr.listeners.AccountListener;
import app.wffr.models.CategoryRatio;
import app.wffr.models.MonthInvoice;
import app.wffr.models.User;
import app.wffr.models.UserAccount;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.ComplaintsActivity;
import app.wffr.ui.activities.EditProfileActivity;
import app.wffr.ui.activities.FavoritesActivity;
import app.wffr.ui.activities.MonthReportsActivity;
import app.wffr.ui.activities.ReportsActivity;
import app.wffr.viewmodels.AccountViewModel;
import app.wffr.viewmodels.PrevOrdersViewModel;

import static br.com.zbra.androidlinq.Linq.stream;

public class AccountFragment extends WffrBaseFragment implements AccountListener {

    private AccountViewModel accountViewModel;
    private AccountFragmentBinding binding;
    private User user;
    private Activity context;
    private CategoryRatioAdapter adapter;
    private ProgressDialog progressDialog;
    private PrevOrdersViewModel prevOrdersViewModel;
    private android.app.AlertDialog builder;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false);
        context = getActivity();
        initRecyclerView();

        // current user data
        user = LocalRepo.getUserData(context);

        // initialize progress dialogs
        progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(R.drawable.playstore);
        progressDialog.setCancelable(false);

        // initialize view models
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        prevOrdersViewModel = new ViewModelProvider(this).get(PrevOrdersViewModel.class);
        accountViewModel.init(this, context);
        prevOrdersViewModel.init(context);


        // show progress loading
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBarCategories.setVisibility(View.VISIBLE);
        binding.progressBarCharts.setVisibility(View.VISIBLE);

        // load user data
        accountViewModel.loadUserAccountData(context, user.getId());

        // load categories ratios data
        accountViewModel.loadCategoriesRatio(context, user.getId());


        // date for current year
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentYear = year.format(new Date());

        // filling user local data
        fillLocalUserData();

        // load orders by months. from first month in the year to the last month. from 1 to 12
        prevOrdersViewModel.loadPrevOrdersByMonth("", user.getId(), currentYear + "-01-01", currentYear + "-12-31");

        prevOrdersViewModel.getMonthReports().observe((LifecycleOwner) context, orders -> {
            if (orders == null || orders.size() == 0){
                binding.statisticsContainer.setVisibility(View.GONE);
                binding.imgNoChar.setVisibility(View.VISIBLE);
                return;
            }
            binding.statisticsContainer.setVisibility(View.VISIBLE);
            binding.imgNoChar.setVisibility(View.GONE);
            fillChartData(orders);
        });

        prevOrdersViewModel.getIsUpdating().observe((LifecycleOwner) context, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBarCharts);
                }else {
                    hideProgressBar(binding.progressBarCharts);
                }
            }
        });

        // buttons events
        binding.btnMonthReport.setOnClickListener(v -> {
            startActivity(new Intent(context, MonthReportsActivity.class));
        });

        binding.btnYearReport.setOnClickListener(v -> {
            startActivity(new Intent(context, ReportsActivity.class));
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfileActivity.class));
            }
        });

        binding.btnComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ComplaintsActivity.class));
            }
        });

        binding.btnFav.setOnClickListener(v -> {
            startActivity(new Intent(context, FavoritesActivity.class));
        });

        View view = binding.getRoot();
        return view;
    }

    private void fillChartData(List<MonthInvoice> orders) {
        List<String> months = Arrays.asList(context.getResources().getStringArray(R.array.months_arr));
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        Random random = new Random(1000);
        for (int i = 1; i <= 12; i++){
            labels.add(months.get(i - 1));
            int finalI = i;
            List<MonthInvoice> vals = stream(orders).where(item-> item.getMounth().equals(finalI + "")).toList();

            float totVal = 0;
            for (int k = 0; k < vals.size(); k++){
                String v = vals.get(k).getTotal();
                totVal += Float.parseFloat(v);
            }
            entries.add(new BarEntry(totVal, i - 1));

            //entries.add(new BarEntry(random.nextFloat(), i - 1));

        }
        BarDataSet bardataset = new BarDataSet(entries, "Costs");
        bardataset.setBarSpacePercent(25);
        BarData data = new BarData(labels, bardataset);
        binding.barchart.setData(data); // set the data and list of labels into chart
        //bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
        binding.barchart.animateY(2500);
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void fillOnlineData(UserAccount userAccount) {
        binding.txtDays.setText((Integer.parseInt(userAccount.getDays()) / 1000 != 0) ? (Integer.parseInt(userAccount.getDays()) / 1000) + "K" : userAccount.getDays());

        binding.imgDays.setOnClickListener(v -> {
            showPointsDialog(userAccount);
        });

        binding.txtTotal.setText(userAccount.getTotal() + " L.E ");
        binding.txtTotalAfter.setText(userAccount.getTotalafter() + " L.E ");
        binding.txtSaveAmount.setText(userAccount.getBetween() + " L.E ");
        binding.txtSaveRatio.setText(userAccount.getFour() + " % ");
        binding.txtOps.setText((int) Float.parseFloat(userAccount.getCount()) + "");
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showPointsDialog(UserAccount userAccount) {
        String points = userAccount.getDays();
        builder = new android.app.AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_points, null);
        builder.setView(view);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = view.findViewById(R.id.btn_done);
        Button btnExchange = view.findViewById(R.id.btn_exchange);
        TextView txtTitle = view.findViewById(R.id.txt_title);
        TextView txtMsg = view.findViewById(R.id.txt_message);

        if (Integer.parseInt(points) >= 10000){
            btnExchange.setVisibility(View.VISIBLE);

            btnExchange.setOnClickListener(v -> {
                accountViewModel.exchangeUserPoints(context, user.getId());
                progressDialog.show();
            });

        }else {
            btnExchange.setVisibility(View.GONE);
        }

        txtMsg.setText(getString(R.string.points_desc));
        txtTitle.setText(points + " " + getString(R.string.point) + " - " + (Integer.parseInt(points) / 200) + " L.E");
        btnOk.setText(getString(R.string.ok_dialog));

        btnOk.setOnClickListener(v -> {
            builder.dismiss();
        });

        builder.show();
    }

    private void fillLocalUserData() {
        binding.btnEdit.setText("  " + user.getName() + "  ");
        String user_img_url = "https://wffr.app" + user.getImg();
        Glide.with(context).load(user_img_url).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(binding.imgProfile);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerCategory.setLayoutManager(layoutManager);
        // set animations
        /*LayoutAnimationController categoriesAnim = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_slide_up);
        binding.recyclerCategory.setLayoutAnimation(categoriesAnim);
        binding.recyclerCategory.scheduleLayoutAnimation();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        user = LocalRepo.getUserData(context);
        fillLocalUserData();
    }


    @Override
    public void OnCategoriesRationGet(List<CategoryRatio> categoryRatioList) {
        adapter = new CategoryRatioAdapter(context, categoryRatioList);
        binding.recyclerCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.progressBarCategories.setVisibility(View.GONE);
    }

    @Override
    public void OnUserDataGet(UserAccount userAccount) {
        fillOnlineData(userAccount);
    }

    @Override
    public void OnPointsExchange(String state) {
        if (state.equals("success")){
            binding.progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            builder.dismiss();
        }
    }

}