package app.wffr.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.adapters.BranchesAdapter;
import app.wffr.databinding.ActivityProductPreviewBinding;
import app.wffr.listeners.FavoriteListener;
import app.wffr.models.Branch;
import app.wffr.models.CouponModel;
import app.wffr.models.FavoriteRequest;
import app.wffr.models.Product;
import app.wffr.models.ProductPreviewResponse;
import app.wffr.models.ShopModel;
import app.wffr.models.User;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;
import app.wffr.utils.utils;
import app.wffr.viewmodels.CouponViewModel;
import app.wffr.viewmodels.FavoritesViewModel;
import app.wffr.viewmodels.GeneralProductViewModel;
import app.wffr.viewmodels.ShopsViewModel;

public class ProductPreviewActivity extends WffrBaseActivity implements OnMapReadyCallback, FavoriteListener {

    private ActivityProductPreviewBinding binding;
    private Product product;
    private BranchesAdapter branchesAdapter;
    private CouponViewModel couponViewModel;
    private FavoritesViewModel favoritesViewModel;
    private String shopId = "";
    private Bitmap bitmap;
    public static SupportMapFragment mapFragment;
    public static GoogleMap mMap;
    public static NestedScrollView nestedScrollView;
    private GeneralProductViewModel generalProductViewModel;
    private ProductPreviewResponse productData;
    private boolean isOpen = false;
    LayoutAnimationController featuredAnim;
    private DatabaseReference couponRef;
    private AlertDialog builder;
    private ValueEventListener valueEventListener;
    private DatabaseReference reference;
    private User currentUser;
    private boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //utils.setAppLocale(LocalRepo.getLanguage(this), this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_preview);
        couponRef = FirebaseDatabase.getInstance().getReference().child("users");

        currentUser = LocalRepo.getUserData(this);

        nestedScrollView = binding.nestedScroll;
        initMaps();

        initRecyclerView();
        Intent intent = getIntent();
        product = intent.getParcelableExtra("product");

        generalProductViewModel = new ViewModelProvider(this).get(GeneralProductViewModel.class);
        generalProductViewModel.init(this);
        generalProductViewModel.loadProductData(product.get_id(), currentUser.getId());
        generalProductViewModel.getProductData().observe(this, new Observer<ProductPreviewResponse>() {
            @Override
            public void onChanged(ProductPreviewResponse productPreviewResponse) {
                if (productPreviewResponse == null || productPreviewResponse.getBranches().size() == 0){
                    Toast.makeText(ProductPreviewActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    return;
                }
                Branch firsBranch = productPreviewResponse.getBranches().get(0);
                String[] loc = firsBranch.getMap().split(",", 2);
                LatLng mapLoc = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
                productData = productPreviewResponse;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mapLoc));
                mMap.setMinZoomPreference(13);
                if(LocalRepo.getLanguage(ProductPreviewActivity.this).equals("en")){
                    mMap.addMarker(new MarkerOptions().position(mapLoc).title(firsBranch.getAddressen()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wffr_location)));
                }else {
                    mMap.addMarker(new MarkerOptions().position(mapLoc).title(firsBranch.getAddress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wffr_location)));
                }
                branchesAdapter = new BranchesAdapter(ProductPreviewActivity.this, productPreviewResponse.getBranches());
                binding.recyclerLocation.setAdapter(branchesAdapter);
                branchesAdapter.notifyDataSetChanged();
                previewData(productPreviewResponse.getShop());
            }
        });

        generalProductViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progLoc);
                }else {
                    hideProgressBar(binding.progLoc);
                }
            }
        });

        couponViewModel = new ViewModelProvider(this).get(CouponViewModel.class);
        couponViewModel.init(this);

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        favoritesViewModel.init(this, this);

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnCobun.setOnClickListener(v -> {

            if (!utils.internetIsConnected()){
                utils.showInternetDialog(this);
                return;
            }

            if (shopId.equals("")){
                return;
            }

            if (productData.getIsStart().equals("true")){
                if (productData.getIsExpir().equals("false")){
                    if (productData.getProduct().getCodeType().equals("qr")){ // qr code
                        generateQrCode(productData.getCopon());
                    }else { // bar Code
                        generateBarCode(productData.getCopon());
                    }
                    previewQRCode(bitmap, Float.parseFloat(productData.getTime()), productData.getCopon());
                }else {
                    if (productData.getProduct().getCodeType().equals("qr")){
                        if (productData.getProduct().getCodeType().equals("qr")){
                            generateNewCoupon(1); // QR
                        }else {
                            generateNewCoupon(0); // BARCODE
                        }
                    }
                }
            }else {
                if (productData.getProduct().getCodeType().equals("qr")){
                    generateNewCoupon(1); // QR
                }else {
                    //Toast.makeText(this, "BAR CODE", Toast.LENGTH_SHORT).show();
                    generateNewCoupon(0); // BARCODE
                }
            }


        });

        binding.btnAddFav.setOnClickListener(v -> {
            if (!utils.internetIsConnected()){
                utils.showInternetDialog(this);
                return;
            }
            binding.progLoc.setVisibility(View.VISIBLE);
            FavoriteRequest favoriteRequest = new FavoriteRequest(
                    "0",
                    product.get_id(),
                    currentUser.getId()
            );

            favoritesViewModel.addRemoveFavorites(favoriteRequest, product);
        });

        binding.btnBranches.setOnClickListener(v -> {
            if (isOpen){
                binding.recyclerLocation.setLayoutAnimation(featuredAnim);
                binding.recyclerLocation.setVisibility(View.GONE);
                isOpen = false;
            }else {
                binding.recyclerLocation.setVisibility(View.VISIBLE);
                binding.recyclerLocation.setLayoutAnimation(featuredAnim);
                isOpen = true;
            }
        });


    }

    private void initMaps() {
        mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void generateNewCoupon(int code_type) {
        couponViewModel.requestNewCoupon(product.get_id(), currentUser.getId());
        couponViewModel.getCoupon().observe(ProductPreviewActivity.this, new Observer<CouponModel>() {
            @Override
            public void onChanged(CouponModel response) {
                productData.setIsStart("true");
                productData.setIsExpir("false");
                if (code_type == 0){ // bar code
                    generateBarCode(response.getCopone());
                }else { // Qr Code
                    generateQrCode(response.getCopone());
                }
                productData.setTime(response.getTime());
                productData.setCopon(response.getCopone());
                previewQRCode(bitmap, Float.parseFloat(productData.getTime()), response.getCopone());
                binding.progLoc.setVisibility(View.GONE);
            }
        });

        couponViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showProgressBar(binding.progLoc);
                }else {
                    hideProgressBar(binding.progLoc);
                }
            }
        });

    }

    private void generateQrCode(String coupon) {
        QRGEncoder qrgEncoder = new QRGEncoder(coupon, null, QRGContents.Type.TEXT, 1000);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void generateBarCode(String coupon) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(coupon, BarcodeFormat.CODE_128, 400, 200);
            Bitmap bmp = Bitmap.createBitmap(400, 200, Bitmap.Config.RGB_565);
            for (int i = 0; i<400; i++){
                for (int j = 0; j<200; j++){
                    bmp.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            bitmap = bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void previewQRCode(Bitmap bitmap, float totalTime, String code) {
        couponRef.child("user_" + currentUser.getId()).child("coupons").child("c_" + product.get_id())
                .child("code").setValue("0");
        builder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(ProductPreviewActivity.this);
        View view = inflater.inflate(R.layout.dialog_qr, null);
        builder.setView(view);
        ImageView img = view.findViewById(R.id.img_qr);
        TextView txt = view.findViewById(R.id.txt_timer);
        TextView txtCode = view.findViewById(R.id.txt_code);
        txtCode.setText(code);

        if (productData.getProduct().getCodeType().equals("qr")){
            txtCode.setVisibility(View.GONE);
        }

        img.setImageBitmap(bitmap);

        new CountDownTimer((long) (totalTime * 1000), 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                productData.setTime(String.valueOf((millisUntilFinished /1000)));
                long sec = (millisUntilFinished /1000) - 60;

                if (sec < 0){
                    sec = millisUntilFinished /1000;
                }

                txt.setText(getString(R.string.time_rem_expire) + " \n " + millisUntilFinished /1000/60 + ":" +  sec);
            }

            @Override
            public void onFinish() {
                productData.setIsStart("false");
                productData.setIsExpir("true");
                txt.setText(getString(R.string.this_coupon_expire));
                builder.dismiss();
            }
        }.start();
        builder.show();
        listenForCoupon();
    }

    private void previewData(ShopModel shop) {
        shopId = shop.getId();
        if(LocalRepo.getLanguage(ProductPreviewActivity.this).equals("en")){
            binding.txtTitle.setText(shop.getNameen());
            binding.txtCategoryTitle.setText(shop.getShopTypeen());
            binding.txtDesc.setText(productData.getProduct().getDisciptionen());
        }else {
            binding.txtTitle.setText(shop.getName());
            binding.txtCategoryTitle.setText(shop.getShopTypear());
            binding.txtDesc.setText(productData.getProduct().getDisciptionar());
        }

        float tot = Float.parseFloat(productData.getProduct().getPrice());
        float disc = Float.parseFloat(productData.getProduct().getDisc());
        float zzz = 100f;
        float totDisc = tot - (tot * (disc/zzz));

        binding.txtPrice.setText(getString(R.string.price) + " " + Math.round(tot) + " L.E");
        binding.txtPriceDiscount.setText(getString(R.string.after_discount) + " " + Math.round(totDisc) + " L.E");

        binding.container1.setVisibility(View.VISIBLE);
        binding.container2.setVisibility(View.VISIBLE);

        if (productData.getProduct().getPrice().equals("0.0") || productData.getProduct().getPrice().equals("0")){
            binding.container1.setVisibility(View.GONE);
        }

        if (productData.getProduct().getDisc().equals("0")){
            binding.txtDiscount.setText(productData.getProduct().getDisc() + " %");
        }

        if (productData.getProduct().getPrice_after() == 0){
            binding.container2.setVisibility(View.GONE);
        }

        if (productData.getProduct().getPrice_after() != 0){

            if (productData.getProduct().getPrice().equals("0.0") && productData.getProduct().getDisc().equals("0")){
                binding.txtDiscount.setText("" + " %");
            }else {
                float ratio = (float) ((1 - (productData.getProduct().getPrice_after() / tot)) * 100);
                binding.txtDiscount.setText(Math.round(ratio) + " %");
            }
            binding.txtPrice.setText(getString(R.string.price) + " " + Math.round(tot) + " L.E");
            binding.txtPriceDiscount.setText(getString(R.string.after_discount) + " " + Math.round(productData.getProduct().getPrice_after()) + " L.E");
        }


        String product_img_url = "https://wffr.app" + shop.getImg();
        Glide.with(this).load(product_img_url).into(binding.imgShop);

        if (productData.getProduct().isIsfav()){
            binding.btnAddFav.setImageResource(R.drawable.img_fav_selected);
            isFav = true;
        }else {
            binding.btnAddFav.setImageResource(R.drawable.favo);
            isFav = false;
        }

        binding.txtExpire.setText(getString(R.string.expire_in) + " : " + productData.getProduct().getProduct_expire());

    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager location = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerLocation.setLayoutManager(location);
        // set animations
        featuredAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_right);
        binding.recyclerLocation.setLayoutAnimation(featuredAnim);
        binding.recyclerLocation.scheduleLayoutAnimation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(34, 34);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Wffr").icon(BitmapDescriptorFactory.fromResource(R.drawable.wffr_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMinZoomPreference(10);
    }

    private void listenForCoupon(){
        reference = couponRef.child("user_" + currentUser.getId()).child("coupons").child("c_" + product.get_id());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("code")){
                    if (dataSnapshot.child("code").getValue().toString().equals("0")){ // waiting

                    }else { // expired or used
                        //reference.removeValue();
                        productData.setIsStart("false");
                        productData.setIsExpir("true");
                        builder.dismiss();
                        //onCouponUsed(productData.getShop().getId(), MainActivity.user.getId());
                        AlertDialog ratingBuilder = new AlertDialog.Builder(ProductPreviewActivity.this).create();
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.dialog_rating, null);
                        ratingBuilder.setView(view);
                        ratingBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        RatingBar ratingBar = view.findViewById(R.id.rat_bar);
                        Button btnSend = view.findViewById(R.id.btn_send);

                        btnSend.setOnClickListener(v -> {
                            float rating = ratingBar.getRating();
                            submitRat(rating, ratingBuilder);
                        });

                        ratingBuilder.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(valueEventListener);
    }

    private void submitRat(float rating, AlertDialog ratingBuilder) {
        ratingBuilder.dismiss();
        ShopsViewModel shopsViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(ShopsViewModel.class);
        shopsViewModel.init(this);
        shopsViewModel.ratShop((int)rating+"", currentUser.getId(), shopId);
        shopsViewModel.getRating().observe((LifecycleOwner) this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Created")){
                    //Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProductPreviewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (valueEventListener != null){
            reference.removeEventListener(valueEventListener);
        }
    }

    @Override
    public void OnFavoritesGet(List<Product> productList) {

    }

    @Override
    public void OnAddRemoveFavorites(String response, Product product) {
        if (isFav){
            isFav = false;
            binding.btnAddFav.setImageResource(R.drawable.favo);
            Toast.makeText(ProductPreviewActivity.this, getString(R.string.fav_removed), Toast.LENGTH_SHORT).show();

        }else {
            binding.btnAddFav.setImageResource(R.drawable.img_fav_selected);
            Toast.makeText(ProductPreviewActivity.this, getString(R.string.fav_added), Toast.LENGTH_SHORT).show();
            isFav = true;
        }
        binding.progLoc.setVisibility(View.GONE);
    }

    @Override
    public void OnLoading(boolean loading) {

    }

    private class DataTasksHandler extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

}