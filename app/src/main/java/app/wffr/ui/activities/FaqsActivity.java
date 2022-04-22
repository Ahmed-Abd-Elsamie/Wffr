package app.wffr.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.ExpandableListAdapter;
import app.wffr.databinding.ActivityFaqsBinding;
import app.wffr.models.ExtendableMenuItem;
import app.wffr.models.FAQModel;
import app.wffr.repositories.LocalRepo;
import app.wffr.viewmodels.FaqsViewModel;

public class FaqsActivity extends WffrBaseActivity {

    private ActivityFaqsBinding binding;
    private HashMap<String, List<ExtendableMenuItem>> listDataChild;
    private ExpandableListAdapter listAdapter;
    private FaqsViewModel faqsViewModel;
    private List<String> listDataHeader = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faqs);

        listDataChild = new HashMap<>();
        faqsViewModel = new ViewModelProvider(this).get(FaqsViewModel.class);
        faqsViewModel.init(this);

        faqsViewModel.getFaqs().observe(this, faqModels -> {
            for (int i = 0; i < faqModels.size(); i++){
                FAQModel faqModel = faqModels.get(i);
                if (LocalRepo.getLanguage(FaqsActivity.this).equals("en")){
                    listDataHeader.add(faqModel.getTitle_en());
                }else {
                    listDataHeader.add(faqModel.getTitle_ar());
                }
                List<ExtendableMenuItem> aboutList = new ArrayList<>();

                if (LocalRepo.getLanguage(FaqsActivity.this).equals("en")){
                    aboutList.add(new ExtendableMenuItem(faqModel.getText_en(), R.drawable.ic_question));
                }else {
                    aboutList.add(new ExtendableMenuItem(faqModel.getText_ar(), R.drawable.ic_question));
                }
                listDataChild.put(listDataHeader.get(i), aboutList); // Header, Child data
            }

            listAdapter = new ExpandableListAdapter(FaqsActivity.this, listDataHeader, listDataChild);
            binding.extFaqList.setAdapter(listAdapter);
        });

        faqsViewModel.getIsUpdating().observe((LifecycleOwner) this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progressBar);
                }else {
                    hideProgressBar(binding.progressBar);
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

}