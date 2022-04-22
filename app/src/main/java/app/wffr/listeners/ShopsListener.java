package app.wffr.listeners;

import java.util.List;
import app.wffr.models.Shop;

public interface ShopsListener {

    void OnLoadingData(boolean loading);
    void OnShopsGet(List<Shop> shops);

}