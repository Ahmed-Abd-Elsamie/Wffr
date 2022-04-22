package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MapsResponse implements Parcelable {

    private String x;
    private String y;
    private String shopid;
    private String phone;
    private String addressar;
    private String addressen;
    private String shopnamear;
    private String shopnameen;
    private String categoryar;
    private String catgoryen;

    public MapsResponse() {
    }

    public MapsResponse(String x, String y, String shopid, String phone, String addressar, String addressen, String shopnamear, String shopnameen, String categoryar, String catgoryen) {
        this.x = x;
        this.y = y;
        this.shopid = shopid;
        this.phone = phone;
        this.addressar = addressar;
        this.addressen = addressen;
        this.shopnamear = shopnamear;
        this.shopnameen = shopnameen;
        this.categoryar = categoryar;
        this.catgoryen = catgoryen;
    }

    protected MapsResponse(Parcel in) {
        x = in.readString();
        y = in.readString();
        shopid = in.readString();
        phone = in.readString();
        addressar = in.readString();
        addressen = in.readString();
        shopnamear = in.readString();
        shopnameen = in.readString();
        categoryar = in.readString();
        catgoryen = in.readString();
    }

    public static final Creator<MapsResponse> CREATOR = new Creator<MapsResponse>() {
        @Override
        public MapsResponse createFromParcel(Parcel in) {
            return new MapsResponse(in);
        }

        @Override
        public MapsResponse[] newArray(int size) {
            return new MapsResponse[size];
        }
    };

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressar() {
        return addressar;
    }

    public void setAddressar(String addressar) {
        this.addressar = addressar;
    }

    public String getAddressen() {
        return addressen;
    }

    public void setAddressen(String addressen) {
        this.addressen = addressen;
    }

    public String getShopnamear() {
        return shopnamear;
    }

    public void setShopnamear(String shopnamear) {
        this.shopnamear = shopnamear;
    }

    public String getShopnameen() {
        return shopnameen;
    }

    public void setShopnameen(String shopnameen) {
        this.shopnameen = shopnameen;
    }

    public String getCategoryar() {
        return categoryar;
    }

    public void setCategoryar(String categoryar) {
        this.categoryar = categoryar;
    }

    public String getCatgoryen() {
        return catgoryen;
    }

    public void setCatgoryen(String catgoryen) {
        this.catgoryen = catgoryen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(x);
        dest.writeString(y);
        dest.writeString(shopid);
        dest.writeString(phone);
        dest.writeString(addressar);
        dest.writeString(addressen);
        dest.writeString(shopnamear);
        dest.writeString(shopnameen);
        dest.writeString(categoryar);
        dest.writeString(catgoryen);
    }
}