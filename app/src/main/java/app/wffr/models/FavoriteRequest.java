package app.wffr.models;


import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteRequest implements Parcelable {

    private String id;
    private String ProductID;
    private String ClientID;

    public FavoriteRequest() {

    }

    public FavoriteRequest(String id, String productID, String clientID) {
        this.id = id;
        ProductID = productID;
        ClientID = clientID;
    }

    protected FavoriteRequest(Parcel in) {
        id = in.readString();
        ProductID = in.readString();
        ClientID = in.readString();
    }

    public static final Creator<FavoriteRequest> CREATOR = new Creator<FavoriteRequest>() {
        @Override
        public FavoriteRequest createFromParcel(Parcel in) {
            return new FavoriteRequest(in);
        }

        @Override
        public FavoriteRequest[] newArray(int size) {
            return new FavoriteRequest[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ProductID);
        dest.writeString(ClientID);
    }
}