package app.wffr.listeners;

import java.util.List;

import app.wffr.models.AdCategoryModel;
import app.wffr.models.AdModel;
import app.wffr.models.Category;

public interface AdsListener {
    void OnLoadingAds(boolean loading);
    void OnAdsGet(List<AdModel> adModelList);
    void OnAdsCategoryGet(List<AdCategoryModel> adCategoryModelList);
}