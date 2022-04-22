package app.wffr.listeners;

import java.util.List;

import app.wffr.models.Category;

public interface CategoriesListener {

    void OnLoadingCategories(boolean loading);
    void OnCategoriesGet(List<Category> categoryList);

}