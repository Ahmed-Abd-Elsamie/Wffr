package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {

    private String id;
    private String name;
    private String nameen;
    private String[] branches;

    public City() {
    }

    public City(String id, String name, String nameen, String[] branches) {
        this.id = id;
        this.name = name;
        this.nameen = nameen;
        this.branches = branches;
    }

    protected City(Parcel in) {
        id = in.readString();
        name = in.readString();
        nameen = in.readString();
        branches = in.createStringArray();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
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

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public String[] getBranches() {
        return branches;
    }

    public void setBranches(String[] branches) {
        this.branches = branches;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(nameen);
        dest.writeStringArray(branches);
    }
}
