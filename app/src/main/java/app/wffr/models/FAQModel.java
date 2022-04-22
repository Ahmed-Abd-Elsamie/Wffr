package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FAQModel implements Parcelable {

    private String title_ar;
    private String title_en;
    private String text_ar;
    private String text_en;

    public FAQModel() {
    }

    public FAQModel(String title_ar, String title_en, String text_ar, String text_en) {
        this.title_ar = title_ar;
        this.title_en = title_en;
        this.text_ar = text_ar;
        this.text_en = text_en;
    }

    protected FAQModel(Parcel in) {
        title_ar = in.readString();
        title_en = in.readString();
        text_ar = in.readString();
        text_en = in.readString();
    }

    public static final Creator<FAQModel> CREATOR = new Creator<FAQModel>() {
        @Override
        public FAQModel createFromParcel(Parcel in) {
            return new FAQModel(in);
        }

        @Override
        public FAQModel[] newArray(int size) {
            return new FAQModel[size];
        }
    };

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getText_ar() {
        return text_ar;
    }

    public void setText_ar(String text_ar) {
        this.text_ar = text_ar;
    }

    public String getText_en() {
        return text_en;
    }

    public void setText_en(String text_en) {
        this.text_en = text_en;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title_ar);
        dest.writeString(title_en);
        dest.writeString(text_ar);
        dest.writeString(text_en);
    }
}