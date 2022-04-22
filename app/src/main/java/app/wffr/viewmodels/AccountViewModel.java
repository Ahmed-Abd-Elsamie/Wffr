package app.wffr.viewmodels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.wffr.listeners.AccountListener;
import app.wffr.listeners.LoginRegisterListener;
import app.wffr.models.CategoryRatio;
import app.wffr.models.User;
import app.wffr.models.UserAccount;
import app.wffr.repositories.LoginRegisterRepo;
import app.wffr.repositories.UserAccountRepo;

public class AccountViewModel extends ViewModel {

    private LoginRegisterRepo loginRegisterRepo;
    private UserAccountRepo userAccountRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LifecycleOwner lifecycleOwner;
    private Activity context;
    private LoginRegisterListener loginRegisterListener;
    private AccountListener accountListener;
    private MutableLiveData<String> pointsState;


    public void init(LoginRegisterListener loginRegisterListener, Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
        this.loginRegisterListener = loginRegisterListener;
    }

    public void init(AccountListener accountListener, Activity context){
        this.lifecycleOwner = (LifecycleOwner) context;
        this.context = context;
        this.accountListener = accountListener;
    }

    public void registerUser(User user){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.registerNewUser(user);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getServerResponse().observe(lifecycleOwner, userResponseModel -> {
            if (userResponseModel == null){
                loginRegisterListener.OnFailure("Server Error");
            }else {
                loginRegisterListener.OnUserRegister(userResponseModel);
            }
        });


    }

    public void updateUser(User user){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.updateUser(user);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getUserUpdateResponse().observe(lifecycleOwner, userResponseModel -> {
            if (userResponseModel == null){
                loginRegisterListener.OnFailure("Server Error");
            }else {
                loginRegisterListener.OnUserUpdated(userResponseModel);
            }
        });

    }

    public void verifyUserPhone(int userId, int code){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.verifyPhone(userId, code);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getVerificationResponse().observe(lifecycleOwner, phoneVerifyResponse -> {
            loginRegisterListener.OnPhoneVerification(phoneVerifyResponse);
        });
    }

    public void sendCode(String phone){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.sendVerifyPhoneCode(phone);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getVerificationResponse().observe(lifecycleOwner, phoneVerifyResponse -> {
            loginRegisterListener.OnCodeResend(phoneVerifyResponse);
        });
    }

    public void resendVerificationCode(String phone){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.sendVerifyPhoneCode(phone);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getVerificationResponse().observe(lifecycleOwner, phoneVerifyResponse -> {
            loginRegisterListener.OnCodeResend(phoneVerifyResponse);
        });
    }

    public void resetUserPassword(String phone, String lang){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.resetPassword(phone, lang);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getPassResponse().observe(lifecycleOwner, state -> {
            loginRegisterListener.OnCodeSent(state);
        });

    }

    public void loginUser(String email, String password){
        loginRegisterRepo = new LoginRegisterRepo();
        loginRegisterRepo.loginUser(email, password);
        loginRegisterRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        loginRegisterRepo.getServerResponse().observe(lifecycleOwner, userResponseModel -> {
            if (userResponseModel == null){
                loginRegisterListener.OnFailure("Server Error");
            }else {
                loginRegisterListener.OnUserRegister(userResponseModel);
            }
        });
    }

    public void loadCategoriesRatio(Activity context, String clientId){
        this.lifecycleOwner = (LifecycleOwner) context;
        userAccountRepo = new UserAccountRepo();
        userAccountRepo.getCategoriesRatios(clientId);
        userAccountRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });

        userAccountRepo.getUserCategoriesRatio().observe(lifecycleOwner, new Observer<List<CategoryRatio>>() {
            @Override
            public void onChanged(List<CategoryRatio> categoryRatios) {
                accountListener.OnCategoriesRationGet(categoryRatios);
            }
        });
    }

    public void loadUserAccountData(Activity context, String clientId){
        this.lifecycleOwner = (LifecycleOwner) context;
        userAccountRepo = new UserAccountRepo();
        userAccountRepo.getMoneyData(clientId);
        userAccountRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });
        userAccountRepo.getUserAccount().observe(lifecycleOwner, new Observer<UserAccount>() {
            @Override
            public void onChanged(UserAccount userAccount) {
                accountListener.OnUserDataGet(userAccount);
            }
        });
    }

    public void exchangeUserPoints(Activity context, String clientId){
        this.lifecycleOwner = (LifecycleOwner) context;
        pointsState = new MutableLiveData<>();
        userAccountRepo = new UserAccountRepo();
        userAccountRepo.exchangePoints(clientId);
        userAccountRepo.getIsUpdating().observe(lifecycleOwner, aBoolean -> {
            if (aBoolean){
                isUpdating.setValue(true);
            }else {
                isUpdating.postValue(false);
            }
        });
        userAccountRepo.getPointsResponse().observe(lifecycleOwner, s -> {
            accountListener.OnPointsExchange(s);
        });
    }

}