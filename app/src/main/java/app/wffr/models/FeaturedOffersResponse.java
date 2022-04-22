package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FeaturedOffersResponse implements Parcelable {

    private List<FeaturedSlider> slidar;
    private List<Product> products;

    public FeaturedOffersResponse() {
    }

    public FeaturedOffersResponse(List<FeaturedSlider> slider, List<Product> products) {
        this.slidar = slider;
        this.products = products;
    }

    protected FeaturedOffersResponse(Parcel in) {
        slidar = in.createTypedArrayList(FeaturedSlider.CREATOR);
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<FeaturedOffersResponse> CREATOR = new Creator<FeaturedOffersResponse>() {
        @Override
        public FeaturedOffersResponse createFromParcel(Parcel in) {
            return new FeaturedOffersResponse(in);
        }

        @Override
        public FeaturedOffersResponse[] newArray(int size) {
            return new FeaturedOffersResponse[size];
        }
    };

    public List<FeaturedSlider> getSlider() {
        return slidar;
    }

    public void setSlider(List<FeaturedSlider> slider) {
        this.slidar = slider;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(slidar);
        dest.writeTypedList(products);
    }


}