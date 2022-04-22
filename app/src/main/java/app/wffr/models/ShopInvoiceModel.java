package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopInvoiceModel implements Parcelable {

    private String id;
    private String namear;
    private String nameen;

    public ShopInvoiceModel() {

    }

    public ShopInvoiceModel(String id, String namear, String nameen) {
        this.id = id;
        this.namear = namear;
        this.nameen = nameen;
    }

    protected ShopInvoiceModel(Parcel in) {
        id = in.readString();
        namear = in.readString();
        nameen = in.readString();
    }

    public static final Creator<ShopInvoiceModel> CREATOR = new Creator<ShopInvoiceModel>() {
        @Override
        public ShopInvoiceModel createFromParcel(Parcel in) {
            return new ShopInvoiceModel(in);
        }

        @Override
        public ShopInvoiceModel[] newArray(int size) {
            return new ShopInvoiceModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamear() {
        return namear;
    }

    public void setNamear(String namear) {
        this.namear = namear;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(namear);
        dest.writeString(nameen);
    }
}
