package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FirebaseNotificationModel implements Parcelable {

    private String general;
    private String male;
    private String female;

    public FirebaseNotificationModel() {
    }

    public FirebaseNotificationModel(String general, String male, String female) {
        this.general = general;
        this.male = male;
        this.female = female;
    }

    protected FirebaseNotificationModel(Parcel in) {
        general = in.readString();
        male = in.readString();
        female = in.readString();
    }

    public static final Creator<FirebaseNotificationModel> CREATOR = new Creator<FirebaseNotificationModel>() {
        @Override
        public FirebaseNotificationModel createFromParcel(Parcel in) {
            return new FirebaseNotificationModel(in);
        }

        @Override
        public FirebaseNotificationModel[] newArray(int size) {
            return new FirebaseNotificationModel[size];
        }
    };

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(general);
        dest.writeString(male);
        dest.writeString(female);
    }


}