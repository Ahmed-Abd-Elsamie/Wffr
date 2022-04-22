package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FeaturedSlider implements Parcelable {

    private String id;
    private String txtar;
    private String txten;
    private String img;
    private boolean active;


    public FeaturedSlider() {
    }

    public FeaturedSlider(String id, String txtar, String txten, String img, boolean active) {
        this.id = id;
        this.txtar = txtar;
        this.txten = txten;
        this.img = img;
        this.active = active;
    }

    protected FeaturedSlider(Parcel in) {
        id = in.readString();
        txtar = in.readString();
        txten = in.readString();
        img = in.readString();
        active = in.readByte() != 0;
    }

    public static final Creator<FeaturedSlider> CREATOR = new Creator<FeaturedSlider>() {
        @Override
        public FeaturedSlider createFromParcel(Parcel in) {
            return new FeaturedSlider(in);
        }

        @Override
        public FeaturedSlider[] newArray(int size) {
            return new FeaturedSlider[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxtar() {
        return txtar;
    }

    public void setTxtar(String txtar) {
        this.txtar = txtar;
    }

    public String getTxten() {
        return txten;
    }

    public void setTxten(String txten) {
        this.txten = txten;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(txtar);
        dest.writeString(txten);
        dest.writeString(img);
        dest.writeByte((byte) (active ? 1 : 0));
    }
}
