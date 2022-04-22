package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Invoice implements Parcelable {

    private String id;
    private String copon;
    private String totalInvois;
    private String disc;
    private String total;
    private String date;
    private String productar;
    private String producten;
    private String clientid;
    private String shopid;

    public Invoice() {
    }

    public Invoice(String id, String copon, String totalInvois, String disc, String total, String date, String productar, String producten, String clientid, String shopid) {
        this.id = id;
        this.copon = copon;
        this.totalInvois = totalInvois;
        this.disc = disc;
        this.total = total;
        this.date = date;
        this.productar = productar;
        this.producten = producten;
        this.clientid = clientid;
        this.shopid = shopid;
    }

    protected Invoice(Parcel in) {
        id = in.readString();
        copon = in.readString();
        totalInvois = in.readString();
        disc = in.readString();
        total = in.readString();
        date = in.readString();
        productar = in.readString();
        producten = in.readString();
        clientid = in.readString();
        shopid = in.readString();
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCopon() {
        return copon;
    }

    public void setCopon(String copon) {
        this.copon = copon;
    }

    public String getTotalInvois() {
        return totalInvois;
    }

    public void setTotalInvois(String totalInvois) {
        this.totalInvois = totalInvois;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductar() {
        return productar;
    }

    public void setProductar(String productar) {
        this.productar = productar;
    }

    public String getProducten() {
        return producten;
    }

    public void setProducten(String producten) {
        this.producten = producten;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(copon);
        dest.writeString(totalInvois);
        dest.writeString(disc);
        dest.writeString(total);
        dest.writeString(date);
        dest.writeString(productar);
        dest.writeString(producten);
        dest.writeString(clientid);
        dest.writeString(shopid);
    }
}