package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchModel implements Parcelable {

    @SerializedName("products")
    @Expose
    private List<Product> products;

    @SerializedName("totalInv")
    @Expose
    private List<Product> totalProducts;

    public SearchModel() {

    }

    public SearchModel(List<Product> products, List<Product> totalProducts) {
        this.products = products;
        this.totalProducts = totalProducts;
    }


    protected SearchModel(Parcel in) {
        products = in.createTypedArrayList(Product.CREATOR);
        totalProducts = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel in) {
            return new SearchModel(in);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(List<Product> totalProducts) {
        this.totalProducts = totalProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(products);
        dest.writeTypedList(totalProducts);
    }


}