package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneCodeResponse implements Parcelable{

    private String state;
    private String message;
    private User model;

    public PhoneCodeResponse(String state, String message, User model) {
        this.state = state;
        this.message = message;
        this.model = model;
    }

    protected PhoneCodeResponse(Parcel in) {
        state = in.readString();
        message = in.readString();
        model = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<PhoneCodeResponse> CREATOR = new Parcelable.Creator<PhoneCodeResponse>() {
        @Override
        public PhoneCodeResponse createFromParcel(Parcel in) {
            return new PhoneCodeResponse(in);
        }

        @Override
        public PhoneCodeResponse[] newArray(int size) {
            return new PhoneCodeResponse[size];
        }
    };

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getModel() {
        return model;
    }

    public void setModel(User model) {
        this.model = model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state);
        dest.writeString(message);
        dest.writeParcelable(model, flags);
    }


}