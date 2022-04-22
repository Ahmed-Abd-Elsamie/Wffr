package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MonthInvoice implements Parcelable {

    private String shopNamear;
    private String shopNameen;
    private String total;
    private String totalfinal;
    private String disc;
    private String mounth;
    private String year;

    public MonthInvoice() {
    }

    public MonthInvoice(String shopNamear, String shopNameen, String total, String totalfinal, String disc, String mounth, String year) {
        this.shopNamear = shopNamear;
        this.shopNameen = shopNameen;
        this.total = total;
        this.totalfinal = totalfinal;
        this.disc = disc;
        this.mounth = mounth;
        this.year = year;
    }

    protected MonthInvoice(Parcel in) {
        shopNamear = in.readString();
        shopNameen = in.readString();
        total = in.readString();
        totalfinal = in.readString();
        disc = in.readString();
        mounth = in.readString();
        year = in.readString();
    }

    public static final Creator<MonthInvoice> CREATOR = new Creator<MonthInvoice>() {
        @Override
        public MonthInvoice createFromParcel(Parcel in) {
            return new MonthInvoice(in);
        }

        @Override
        public MonthInvoice[] newArray(int size) {
            return new MonthInvoice[size];
        }
    };

    public String getShopNamear() {
        return shopNamear;
    }

    public void setShopNamear(String shopNamear) {
        this.shopNamear = shopNamear;
    }

    public String getShopNameen() {
        return shopNameen;
    }

    public void setShopNameen(String shopNameen) {
        this.shopNameen = shopNameen;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalfinal() {
        return totalfinal;
    }

    public void setTotalfinal(String totalfinal) {
        this.totalfinal = totalfinal;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getMounth() {
        return mounth;
    }

    public void setMounth(String mounth) {
        this.mounth = mounth;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopNamear);
        dest.writeString(shopNameen);
        dest.writeString(total);
        dest.writeString(totalfinal);
        dest.writeString(disc);
        dest.writeString(mounth);
        dest.writeString(year);
    }
}