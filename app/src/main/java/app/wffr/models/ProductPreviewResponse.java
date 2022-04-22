package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProductPreviewResponse implements Parcelable {

    private String isExpir;
    private String isStart;
    private String time;
    private String copon;
    private ProductModel product;
    private ShopModel shop;
    private List<Branch> branches;

    public ProductPreviewResponse() {
    }

    public ProductPreviewResponse(String isExpir, String isStart, String time, String copon, ProductModel product, ShopModel shop, List<Branch> branches) {
        this.isExpir = isExpir;
        this.isStart = isStart;
        this.time = time;
        this.copon = copon;
        this.product = product;
        this.shop = shop;
        this.branches = branches;
    }

    protected ProductPreviewResponse(Parcel in) {
        isExpir = in.readString();
        isStart = in.readString();
        time = in.readString();
        copon = in.readString();
        product = in.readParcelable(ProductModel.class.getClassLoader());
        shop = in.readParcelable(ShopModel.class.getClassLoader());
        branches = in.createTypedArrayList(Branch.CREATOR);
    }

    public static final Creator<ProductPreviewResponse> CREATOR = new Creator<ProductPreviewResponse>() {
        @Override
        public ProductPreviewResponse createFromParcel(Parcel in) {
            return new ProductPreviewResponse(in);
        }

        @Override
        public ProductPreviewResponse[] newArray(int size) {
            return new ProductPreviewResponse[size];
        }
    };

    public String getIsExpir() {
        return isExpir;
    }

    public void setIsExpir(String isExpir) {
        this.isExpir = isExpir;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCopon() {
        return copon;
    }

    public void setCopon(String copon) {
        this.copon = copon;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public ShopModel getShop() {
        return shop;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isExpir);
        dest.writeString(isStart);
        dest.writeString(time);
        dest.writeString(copon);
        dest.writeParcelable(product, flags);
        dest.writeParcelable(shop, flags);
        dest.writeTypedList(branches);
    }
}