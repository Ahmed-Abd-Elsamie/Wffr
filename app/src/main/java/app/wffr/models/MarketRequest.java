package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MarketRequest implements Parcelable {

    private String id;
    private String phone;
    private String note;
    private String email;
    private String name;

    public MarketRequest() {
    }

    public MarketRequest(String id, String phone, String note, String email, String name) {
        this.id = id;
        this.phone = phone;
        this.note = note;
        this.email = email;
        this.name = name;
    }

    protected MarketRequest(Parcel in) {
        id = in.readString();
        phone = in.readString();
        note = in.readString();
        email = in.readString();
        name = in.readString();
    }

    public static final Creator<MarketRequest> CREATOR = new Creator<MarketRequest>() {
        @Override
        public MarketRequest createFromParcel(Parcel in) {
            return new MarketRequest(in);
        }

        @Override
        public MarketRequest[] newArray(int size) {
            return new MarketRequest[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(phone);
        dest.writeString(note);
        dest.writeString(email);
        dest.writeString(name);
    }
}