package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Branch implements Parcelable {

    private String id;
    private String[] phone;
    private String address;
    private String addressen;
    private String map;
    private String shopID;
    private String cityID;
    private String city;
    private String shop;
    private String[] users_shop;

    public Branch() {
    }

    public Branch(String id, String[] phone, String address, String addressen, String map, String shopID, String cityID, String city, String shop, String[] users_shop) {
        this.id = id;
        this.phone = phone;
        this.address = address;
        this.addressen = addressen;
        this.map = map;
        this.shopID = shopID;
        this.cityID = cityID;
        this.city = city;
        this.shop = shop;
        this.users_shop = users_shop;
    }

    protected Branch(Parcel in) {
        id = in.readString();
        phone = in.createStringArray();
        address = in.readString();
        addressen = in.readString();
        map = in.readString();
        shopID = in.readString();
        cityID = in.readString();
        city = in.readString();
        shop = in.readString();
        users_shop = in.createStringArray();
    }

    public static final Creator<Branch> CREATOR = new Creator<Branch>() {
        @Override
        public Branch createFromParcel(Parcel in) {
            return new Branch(in);
        }

        @Override
        public Branch[] newArray(int size) {
            return new Branch[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getPhone() {
        return phone;
    }

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressen() {
        return addressen;
    }

    public void setAddressen(String addressen) {
        this.addressen = addressen;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String[] getUsers_shop() {
        return users_shop;
    }

    public void setUsers_shop(String[] users_shop) {
        this.users_shop = users_shop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringArray(phone);
        dest.writeString(address);
        dest.writeString(addressen);
        dest.writeString(map);
        dest.writeString(shopID);
        dest.writeString(cityID);
        dest.writeString(city);
        dest.writeString(shop);
        dest.writeStringArray(users_shop);
    }
}