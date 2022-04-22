package app.wffr;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.wffr.adapters.ExpandableListAdapter;
import app.wffr.databinding.ActivityMainBinding;
import app.wffr.models.ExtendableMenuItem;
import app.wffr.models.FeedbackModel;
import app.wffr.models.FirebaseNotificationModel;
import app.wffr.models.SocialModel;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.AboutUsActivity;
import app.wffr.ui.activities.AddShopActivity;
import app.wffr.ui.activities.AppWebBrowser;
import app.wffr.ui.activities.ComplaintsActivity;
import app.wffr.ui.activities.ContactUsActivity;
import app.wffr.ui.activities.FaqsActivity;
import app.wffr.ui.activities.FavoritesActivity;
import app.wffr.ui.activities.LoginActivity;
import app.wffr.ui.activities.NotificationsActivity;
import app.wffr.ui.activities.PoliciesActivity;
import app.wffr.ui.activities.PrevOrdersActivity;
import app.wffr.ui.activities.SettingsActivity;
import app.wffr.ui.fragments.AccountFragment;
import app.wffr.ui.fragments.FeaturedFragment;
import app.wffr.ui.fragments.OffersFragment;
import app.wffr.ui.fragments.SearchFragment;
import app.wffr.viewmodels.ComplaintsViewModel;
import app.wffr.viewmodels.SocialViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<ExtendableMenuItem>> listDataChild;
    public static User user;
    private SocialViewModel socialViewModel;
    private boolean isSocial = false;
    private String facebook = "";
    private String twitter = "";
    private String instagram = "";
    private boolean isNotifyGet = false;

    private DatabaseReference notificationsReference;
    private Map<String, Object> firebaseUserData;
    private Map<String, Object> firebaseGenData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        socialViewModel = new ViewModelProvider(this).get(SocialViewModel.class);
        socialViewModel.init(this);


        socialViewModel.getSocialContact().observe(this, new Observer<List<SocialModel>>() {
            @Override
            public void onChanged(List<SocialModel> socialModels) {
                facebook = socialModels.get(4).getUrl();
                twitter = socialModels.get(1).getUrl();
                instagram = socialModels.get(2).getUrl();
            }
        });

        socialViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isSocial = false;
                    binding.progressBarSocial.setVisibility(View.VISIBLE);
                }else {
                    isSocial = true;
                    binding.progressBarSocial.setVisibility(View.GONE);
                }
            }
        });

        binding.imgFace.setOnClickListener(v -> {
            if (isSocial){
                if (facebook == null || facebook.equals("")){

                }else {
                    Uri uri = Uri.parse(facebook);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    /*Intent intent = new Intent(this, AppWebBrowser.class);
                    intent.putExtra("url", facebook);
                    startActivity(intent);*/
                }
            }
        });

        binding.imgTwiter.setOnClickListener(v -> {
            if (isSocial){
                if (twitter == null || twitter.equals("")){

                }else {
                    Uri uri = Uri.parse(twitter);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                    /*Intent intent = new Intent(this, AppWebBrowser.class);
                    intent.putExtra("url", twitter);
                    startActivity(intent);*/
                }
            }
        });

        binding.imgInstagram.setOnClickListener(v -> {
            if (isSocial){
                if (instagram == null || instagram.equals("")){

                }else {
                    Uri uri = Uri.parse(instagram);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    /*Intent intent = new Intent(this, AppWebBrowser.class);
                    intent.putExtra("url", instagram);
                    startActivity(intent);*/
                }
            }
        });

        try {
            checkUserState();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        initExtendableMenu();

        // default screen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OffersFragment(), "offers").commit();

        binding.navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_offers:
                    OffersFragment offersFragment = (OffersFragment)getSupportFragmentManager().findFragmentByTag("offers");
                    if (offersFragment != null && offersFragment.isVisible()) {
                        return true;
                    }
                    item.setIcon(R.drawable.img_home_sel);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OffersFragment(), "offers").commit();
                    return true;
                case R.id.navigation_search:
                    SearchFragment searchFragment = (SearchFragment)getSupportFragmentManager().findFragmentByTag("search");
                    if (searchFragment != null && searchFragment.isVisible()) {
                        return true;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), "search").commit();
                    return true;
                case R.id.navigation_featured:
                    FeaturedFragment featuredFragment = (FeaturedFragment)getSupportFragmentManager().findFragmentByTag("featured");
                    if (featuredFragment != null && featuredFragment.isVisible()) {
                        return true;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeaturedFragment(), "featured").commit();
                    return true;
                case R.id.navigation_account:
                    if (user != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment(), "account").commit();
                        return true;
                    }else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        return false;
                    }
                default:
                    return false;
            }

        });

        binding.imgNav.setOnClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(Gravity.START)) {
                binding.drawerLayout.closeDrawer(Gravity.START);
            } else {
                binding.drawerLayout.openDrawer(Gravity.START);
            }
        });

        binding.btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    if (isNotifyGet){
                        FirebaseNotificationModel userModel = new FirebaseNotificationModel(
                                firebaseUserData.get("general").toString(),
                                firebaseUserData.get("male").toString(),
                                firebaseUserData.get("female").toString()
                        );
                        FirebaseNotificationModel genModel = new FirebaseNotificationModel(
                                firebaseGenData.get("general").toString(),
                                firebaseGenData.get("male").toString(),
                                firebaseGenData.get("female").toString()
                        );

                        Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                        intent.putExtra("my_notify", userModel);
                        intent.putExtra("general_notify", genModel);
                        startActivity(intent);
                    }
                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else {
                    showLogoutDialog();
                    binding.drawerLayout.closeDrawer(Gravity.START);
                }
            }
        });

        binding.btnHome.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OffersFragment()).commit();
            binding.drawerLayout.closeDrawer(Gravity.START);
            binding.navView.setSelectedItemId(R.id.navigation_offers);
        });

        binding.btnPreOrders.setOnClickListener(v -> {
            if (user != null){
                startActivity(new Intent(MainActivity.this, PrevOrdersActivity.class));
            }else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        binding.btnFav.setOnClickListener(v -> {
            if (user != null){
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
            }else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        binding.btnContact.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
        });

        binding.btnSettings.setOnClickListener(v -> {
            if (user != null){
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                finish();
            }else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        binding.btnAddShop.setOnClickListener(v -> {
            if (user != null){
                startActivity(new Intent(MainActivity.this, AddShopActivity.class));
            }else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        binding.btnLoginShop.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://wffr.app/?action=LoginClient"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        binding.imgLogo.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OffersFragment()).commit();
            //binding.drawerLayout.closeDrawer(Gravity.START);
            binding.navView.setSelectedItemId(R.id.navigation_offers);
        });

        binding.imgDrawerProfile.setOnClickListener(v -> {
            if (user != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
                binding.drawerLayout.closeDrawer(Gravity.START);
                binding.navView.setSelectedItemId(R.id.navigation_account);
            }else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        String tokenId = FirebaseInstanceId.getInstance().getToken();
        System.out.println("TOKEN:" + tokenId);



        if (user != null){
            System.out.println("UUUUUUUUUUUUU : " + user.getId());
            // set notification counter
            notificationsReference = FirebaseDatabase.getInstance().getReference();
            notificationsReference.child("users").child("user_" + user.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.toString());
                    firebaseUserData = (Map<String, Object>)dataSnapshot.getValue();
                    int my_gen_count = Integer.parseInt(firebaseUserData.get("general").toString());
                    int my_count = Integer.parseInt(firebaseUserData.get((user.getGender() ? "male" : "female")).toString());

                    notificationsReference.child("general_count").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot genSnap) {

                            firebaseGenData = (Map<String, Object>)genSnap.getValue();
                            int count = Integer.parseInt(firebaseGenData.get(user.getGender() ? "male" : "female").toString());
                            int gen_count = Integer.parseInt(firebaseGenData.get("general").toString());

                            int num = gen_count - my_gen_count + count - my_count;
                            binding.txtNotify.setText(num + "");
                            if (num == 0){
                                binding.txtNotify.setVisibility(View.GONE);
                            }else {
                                binding.txtNotify.setVisibility(View.VISIBLE);
                            }
                            binding.progNotify.setVisibility(View.GONE);
                            isNotifyGet = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // subscribe for general notifications
            FirebaseMessaging.getInstance().subscribeToTopic("notifications")
                    .addOnCompleteListener(task -> {
                        String msg = "success";
                        if (!task.isSuccessful()) {
                            msg = "filed subscribe";
                        }
                    });

            // subscribe for gender notifications
            FirebaseMessaging.getInstance().subscribeToTopic(user.getGender() ? "male" : "female")
                    .addOnCompleteListener(task -> {
                        String msg = "success";
                        if (!task.isSuccessful()) {
                            msg = "filed subscribe";
                        }
                    });
        }else {
            // unsubscribe for notifications
            binding.progNotify.setVisibility(View.GONE);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications")
                    .addOnCompleteListener(task -> {
                String msg = "success";
                if (!task.isSuccessful()) {
                    msg = "filed subscribe";
                }
            });
            FirebaseMessaging.getInstance().unsubscribeFromTopic("male")
                    .addOnCompleteListener(task -> {
                        String msg = "success";
                        if (!task.isSuccessful()) {
                            msg = "filed subscribe";
                        }
                    });
            FirebaseMessaging.getInstance().unsubscribeFromTopic("female")
                    .addOnCompleteListener(task -> {
                        String msg = "success";
                        if (!task.isSuccessful()) {
                            msg = "filed subscribe";
                        }
                    });
        }

        binding.aboutUsMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                binding.sideScroll.fullScroll(View.FOCUS_DOWN);
                return false;
            }
        });
    }

    private void showLogoutDialog() {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_message, null);
        builder.setView(view);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = view.findViewById(R.id.btn_done);
        TextView txtMsg = view.findViewById(R.id.txt_message);
        ImageView imgMsg = view.findViewById(R.id.img_message);

        txtMsg.setText(getString(R.string.logout_alert));
        btnOk.setText(getString(R.string.sign_out));
        imgMsg.setImageResource(R.drawable.ic_baseline_login_35);


        btnOk.setOnClickListener(v -> {
            logoutUser();
            builder.dismiss();
        });

        builder.show();
    }

    private void logoutUser() {
        LocalRepo.saveUserData(this, null);
        LocalRepo.saveCity(this, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OffersFragment()).commit();
        binding.navView.setSelectedItemId(R.id.navigation_offers);
        user = null;
        binding.imgDrawerProfile.setImageResource(R.drawable.ic_logo);
        binding.txtUserName.setText(R.string.app_name);
        binding.txtLoginState.setText(R.string.sign_in);

        // unsubscribe for notifications
        FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("male");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("female");

    }

    private void checkUserState() throws ParseException {
        user = LocalRepo.getUserData(this);
        if (user == null){
            binding.imgDrawerProfile.setImageResource(R.drawable.ic_logo);
            binding.txtUserName.setText(R.string.app_name);
        }else {
            String user_img_url = "https://wffr.app" + user.getImg();
            Glide.with(this).load(user_img_url).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(binding.imgDrawerProfile);
            binding.txtUserName.setText(user.getName());
            binding.txtLoginState.setText(R.string.sign_out);

            String crtDate = user.getCreationDate().substring(0, 10);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddd", Locale.ENGLISH);
            Date firstDate = sdf.parse(crtDate);
            long diffInMillies = Math.abs(System.currentTimeMillis() - firstDate.getTime());
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies);
            if (diffInDays >= 7 && !(LocalRepo.getFeedback(this))){
                showFeedbackDialog();
            }
        }
    }

    private void showFeedbackDialog() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_feedback, null);
        builder.setView(view);

        LinearLayout btnLike = view.findViewById(R.id.btn_like);
        LinearLayout btnFair = view.findViewById(R.id.btn_fair);
        LinearLayout btnDislike = view.findViewById(R.id.btn_dislike);

        FeedbackModel feedbackModel = new FeedbackModel(
                "0",
                "0",
                "0"
        );

        btnLike.setOnClickListener(v -> {
            feedbackModel.setLike("1");
            sendFeedBack(feedbackModel);
            builder.dismiss();
        });

        btnFair.setOnClickListener(v -> {
            feedbackModel.setFair("1");
            sendFeedBack(feedbackModel);
            builder.dismiss();
        });

        btnDislike.setOnClickListener(v -> {
            feedbackModel.setDislike("1");
            sendFeedBack(feedbackModel);
            builder.dismiss();
        });

        builder.show();
    }

    private void sendFeedBack(FeedbackModel feedbackModel) {
        ComplaintsViewModel complaintsViewModel = new ViewModelProvider(this).get(ComplaintsViewModel.class);
        complaintsViewModel.sendFeedback(this, feedbackModel);

        complaintsViewModel.getFeedResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("created")){
                    LocalRepo.saveFeedback(MainActivity.this, true);
                }
            }
        });
    }

    private void initExtendableMenu() {
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        binding.aboutUsMenu.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(getString(R.string.about_wffr));

        // Adding child data
        List<ExtendableMenuItem> aboutList = new ArrayList<ExtendableMenuItem>();
        aboutList.add(new ExtendableMenuItem(getString(R.string.suggest), R.drawable.ic_baseline_textsms_35));
        aboutList.add(new ExtendableMenuItem(getString(R.string.about_wffr), R.drawable.ic_baseline_info_35));
        aboutList.add(new ExtendableMenuItem(getString(R.string.policies), R.drawable.ic_outline_article_35));
        aboutList.add(new ExtendableMenuItem(getString(R.string.faqs), R.drawable.ic_baseline_question_answer_35));

        listDataChild.put(listDataHeader.get(0), aboutList); // Header, Child data

        binding.aboutUsMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (childPosition == 0){
                    if (user == null){
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }else {
                        startActivity(new Intent(MainActivity.this, ComplaintsActivity.class));
                    }
                }
                if (childPosition == 1){
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                }
                if (childPosition == 2){
                    startActivity(new Intent(MainActivity.this, PoliciesActivity.class));
                }
                if (childPosition == 3){
                    startActivity(new Intent(MainActivity.this, FaqsActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user == null){
            binding.imgDrawerProfile.setImageResource(R.drawable.ic_logo);
            binding.txtUserName.setText(R.string.app_name);
            binding.txtLoginState.setText(R.string.sign_in);
        }else {
            binding.txtLoginState.setText(R.string.sign_out);
            String user_img_url = "https://wffr.app" + user.getImg();
            Glide.with(this).load(user_img_url).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(binding.imgDrawerProfile);
            binding.txtUserName.setText(user.getName());
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.START)) {
            binding.drawerLayout.closeDrawer(Gravity.START);
        }else {
            super.onBackPressed();
        }
    }


}