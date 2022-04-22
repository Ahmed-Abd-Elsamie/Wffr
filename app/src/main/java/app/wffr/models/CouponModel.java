package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CouponModel implements Parcelable {

    private String copone;
    private String time;

    public CouponModel() {
    }

    public CouponModel(String copone, String time) {
        this.copone = copone;
        this.time = time;
    }

    protected CouponModel(Parcel in) {
        copone = in.readString();
        time = in.readString();
    }

    public static final Creator<CouponModel> CREATOR = new Creator<CouponModel>() {
        @Override
        public CouponModel createFromParcel(Parcel in) {
            return new CouponModel(in);
        }

        @Override
        public CouponModel[] newArray(int size) {
            return new CouponModel[size];
        }
    };

    public String getCopone() {
        return copone;
    }

    public void setCopone(String copone) {
        this.copone = copone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(copone);
        dest.writeString(time);
    }
}