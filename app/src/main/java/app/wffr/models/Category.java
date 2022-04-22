package app.wffr.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    private String id;
    private String name;
    private String name_en;
    private String active;
    private String img;
    private boolean checked;

    public Category() {
    }

    public Category(String id, String name, String name_en, String active, String img, boolean checked) {
        this.id = id;
        this.name = name;
        this.name_en = name_en;
        this.active = active;
        this.img = img;
        this.checked = checked;
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        name_en = in.readString();
        active = in.readString();
        img = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(name_en);
        dest.writeString(active);
        dest.writeString(img);
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}