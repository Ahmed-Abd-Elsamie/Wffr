package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DashboardSettings implements Parcelable {

    private String id;
    private String expairCopone;
    private String about_Company;
    private String about_Company_en;
    private String conditions_Rules;
    private String conditions_Rules_en;

    public DashboardSettings() {

    }

    public DashboardSettings(String id, String expairCopone, String about_Company, String about_Company_en, String conditions_Rules, String conditions_Rules_en) {
        this.id = id;
        this.expairCopone = expairCopone;
        this.about_Company = about_Company;
        this.about_Company_en = about_Company_en;
        this.conditions_Rules = conditions_Rules;
        this.conditions_Rules_en = conditions_Rules_en;
    }

    protected DashboardSettings(Parcel in) {
        id = in.readString();
        expairCopone = in.readString();
        about_Company = in.readString();
        about_Company_en = in.readString();
        conditions_Rules = in.readString();
        conditions_Rules_en = in.readString();
    }

    public static final Creator<DashboardSettings> CREATOR = new Creator<DashboardSettings>() {
        @Override
        public DashboardSettings createFromParcel(Parcel in) {
            return new DashboardSettings(in);
        }

        @Override
        public DashboardSettings[] newArray(int size) {
            return new DashboardSettings[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpairCopone() {
        return expairCopone;
    }

    public void setExpairCopone(String expairCopone) {
        this.expairCopone = expairCopone;
    }

    public String getAbout_Company() {
        return about_Company;
    }

    public void setAbout_Company(String about_Company) {
        this.about_Company = about_Company;
    }

    public String getAbout_Company_en() {
        return about_Company_en;
    }

    public void setAbout_Company_en(String about_Company_en) {
        this.about_Company_en = about_Company_en;
    }

    public String getConditions_Rules() {
        return conditions_Rules;
    }

    public void setConditions_Rules(String conditions_Rules) {
        this.conditions_Rules = conditions_Rules;
    }

    public String getConditions_Rules_en() {
        return conditions_Rules_en;
    }

    public void setConditions_Rules_en(String conditions_Rules_en) {
        this.conditions_Rules_en = conditions_Rules_en;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(expairCopone);
        dest.writeString(about_Company);
        dest.writeString(about_Company_en);
        dest.writeString(conditions_Rules);
        dest.writeString(conditions_Rules_en);
    }
}