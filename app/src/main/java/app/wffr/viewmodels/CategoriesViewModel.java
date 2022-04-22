package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.listeners.CategoriesListener;
import app.wffr.models.Category;
import app.wffr.repositories.CategoriesRepo;
import app.wffr.utils.Constants;

public class CategoriesViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories;
    private CategoriesRepo categoriesRepo;
    private LifecycleOwner lifecycleOwner;

    public void init(Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    public void loadCategories(int type, CategoriesListener categoriesListener) {
        if (categories == null || type == Constants.REFRESH) {
            categories = new MutableLiveData<>();
        }else {
            return;
        }
        categoriesRepo = CategoriesRepo.getInstance(type);
        categoriesRepo.getIsUpdating().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                categoriesListener.OnLoadingCategories(aBoolean);
            }
        });
        categoriesRepo.getCategories().observe(lifecycleOwner, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoriesListener.OnCategoriesGet(categories);
            }
        });
    }

}
