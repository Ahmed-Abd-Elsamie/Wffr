package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialModel implements Parcelable {

    private String img;
    private String url;
    private String name;

    public SocialModel() {
    }

    public SocialModel(String img, String url, String name) {
        this.img = img;
        this.url = url;
        this.name = name;
    }

    protected SocialModel(Parcel in) {
        img = in.readString();
        url = in.readString();
        name = in.readString();
    }

    public static final Creator<SocialModel> CREATOR = new Creator<SocialModel>() {
        @Override
        public SocialModel createFromParcel(Parcel in) {
            return new SocialModel(in);
        }

        @Override
        public SocialModel[] newArray(int size) {
            return new SocialModel[size];
        }
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        dest.writeString(img);
        dest.writeString(url);
        dest.writeString(name);
    }


}