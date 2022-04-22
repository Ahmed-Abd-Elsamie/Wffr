package app.wffr.listeners;

import java.util.List;

import app.wffr.models.FeaturedSlider;
import app.wffr.models.Product;

public interface FeaturedProductListener {

    void OnLoadingData(boolean loading);
    void OnProductsGet(List<Product> products, List<FeaturedSlider> adsList);

}