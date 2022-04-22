package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserAccount implements Parcelable {

    private String total;
    private String totalafter;
    private String between;
    private String four;

    @SerializedName("points")
    private String days;

    private String count;

    public UserAccount() {
    }

    public UserAccount(String total, String totalafter, String between, String four, String days, String count) {
        this.total = total;
        this.totalafter = totalafter;
        this.between = between;
        this.four = four;
        this.days = days;
        this.count = count;
    }

    protected UserAccount(Parcel in) {
        total = in.readString();
        totalafter = in.readString();
        between = in.readString();
        four = in.readString();
        days = in.readString();
        count = in.readString();
    }

    public static final Creator<UserAccount> CREATOR = new Creator<UserAccount>() {
        @Override
        public UserAccount createFromParcel(Parcel in) {
            return new UserAccount(in);
        }

        @Override
        public UserAccount[] newArray(int size) {
            return new UserAccount[size];
        }
    };

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalafter() {
        return totalafter;
    }

    public void setTotalafter(String totalafter) {
        this.totalafter = totalafter;
    }

    public String getBetween() {
        return between;
    }

    public void setBetween(String between) {
        this.between = between;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(total);
        dest.writeString(totalafter);
        dest.writeString(between);
        dest.writeString(four);
        dest.writeString(days);
        dest.writeString(count);
    }
}