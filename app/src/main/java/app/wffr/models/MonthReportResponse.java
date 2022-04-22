package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonthReportResponse implements Parcelable {

    @SerializedName("invoiss")
    @Expose
    private List<MonthInvoice> monthInvoices;

    @SerializedName("shops")
    @Expose
    private List<ShopInvoiceModel> shopInvoiceModels;

    public MonthReportResponse() {
    }

    public MonthReportResponse(List<MonthInvoice> monthInvoices, List<ShopInvoiceModel> shopInvoiceModels) {
        this.monthInvoices = monthInvoices;
        this.shopInvoiceModels = shopInvoiceModels;
    }

    protected MonthReportResponse(Parcel in) {
        monthInvoices = in.createTypedArrayList(MonthInvoice.CREATOR);
        shopInvoiceModels = in.createTypedArrayList(ShopInvoiceModel.CREATOR);
    }

    public static final Creator<MonthReportResponse> CREATOR = new Creator<MonthReportResponse>() {
        @Override
        public MonthReportResponse createFromParcel(Parcel in) {
            return new MonthReportResponse(in);
        }

        @Override
        public MonthReportResponse[] newArray(int size) {
            return new MonthReportResponse[size];
        }
    };

    public List<MonthInvoice> getMonthInvoices() {
        return monthInvoices;
    }

    public void setMonthInvoices(List<MonthInvoice> monthInvoices) {
        this.monthInvoices = monthInvoices;
    }

    public List<ShopInvoiceModel> getShopInvoiceModels() {
        return shopInvoiceModels;
    }

    public void setShopInvoiceModels(List<ShopInvoiceModel> shopInvoiceModels) {
        this.shopInvoiceModels = shopInvoiceModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(monthInvoices);
        dest.writeTypedList(shopInvoiceModels);
    }
}