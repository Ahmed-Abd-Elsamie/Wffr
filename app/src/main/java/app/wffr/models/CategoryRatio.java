package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryRatio implements Parcelable {

    private String depNamear;
    private String depNameen;
    private String ratio;

    public CategoryRatio() {

    }

    public CategoryRatio(String depNamear, String depNameen, String ratio) {
        this.depNamear = depNamear;
        this.depNameen = depNameen;
        this.ratio = ratio;
    }

    protected CategoryRatio(Parcel in) {
        depNamear = in.readString();
        depNameen = in.readString();
        ratio = in.readString();
    }

    public static final Creator<CategoryRatio> CREATOR = new Creator<CategoryRatio>() {
        @Override
        public CategoryRatio createFromParcel(Parcel in) {
            return new CategoryRatio(in);
        }

        @Override
        public CategoryRatio[] newArray(int size) {
            return new CategoryRatio[size];
        }
    };

    public String getDepNamear() {
        return depNamear;
    }

    public void setDepNamear(String depNamear) {
        this.depNamear = depNamear;
    }

    public String getDepNameen() {
        return depNameen;
    }

    public void setDepNameen(String depNameen) {
        this.depNameen = depNameen;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(depNamear);
        dest.writeString(depNameen);
        dest.writeString(ratio);
    }
}