package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Complaint implements Parcelable {
    private String title;
    private String text;

    public Complaint() {
    }

    public Complaint(String title, String text) {
        this.title = title;
        this.text = text;
    }

    protected Complaint(Parcel in) {
        title = in.readString();
        text = in.readString();
    }

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
    }
}
