package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackModel implements Parcelable {

    @SerializedName("first")
    @Expose
    private String like;

    @SerializedName("second")
    @Expose
    private String fair;

    @SerializedName("last")
    @Expose
    private String dislike;


    public FeedbackModel() {
    }

    public FeedbackModel(String like, String fair, String dislike) {
        this.like = like;
        this.fair = fair;
        this.dislike = dislike;
    }

    protected FeedbackModel(Parcel in) {
        like = in.readString();
        fair = in.readString();
        dislike = in.readString();
    }

    public static final Creator<FeedbackModel> CREATOR = new Creator<FeedbackModel>() {
        @Override
        public FeedbackModel createFromParcel(Parcel in) {
            return new FeedbackModel(in);
        }

        @Override
        public FeedbackModel[] newArray(int size) {
            return new FeedbackModel[size];
        }
    };

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getFair() {
        return fair;
    }

    public void setFair(String fair) {
        this.fair = fair;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(like);
        dest.writeString(fair);
        dest.writeString(dislike);
    }


}