package app.wffr.listeners;

import java.util.List;

import app.wffr.models.CategoryRatio;
import app.wffr.models.UserAccount;

public interface AccountListener {

    void OnCategoriesRationGet(List<CategoryRatio> categoryRatioList);
    void OnUserDataGet(UserAccount userAccount);
    void OnPointsExchange(String state);

}