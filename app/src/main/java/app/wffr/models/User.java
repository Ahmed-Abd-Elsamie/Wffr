package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class User implements Parcelable{

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("img")
    @Expose
    private String img;

    @SerializedName("gender")
    @Expose
    private boolean gender;

    @SerializedName("creationDate")
    @Expose
    private String creationDate;

    @SerializedName("notif_count")
    @Expose
    private String notif_count;

    @SerializedName("fromDate")
    @Expose
    private String fromDate;

    @SerializedName("toDate")
    @Expose
    private String toDate;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("assesses")
    @Expose
    private String[] assesses;

    @SerializedName("notif_client")
    @Expose
    private String[] notif_client;

    @SerializedName("shopInvois")
    @Expose
    private String[] shopInvois;

    @SerializedName("whachs")
    @Expose
    private String[] whachs;

    @SerializedName("client_Copnes")
    @Expose
    private String[] client_Copnes;

    @SerializedName("favorites")
    @Expose
    private String[] favorites;

    @SerializedName("subscriptions")
    @Expose
    private String[] subscriptions;

    @SerializedName("password2")
    @Expose
    private String password2;

    @SerializedName("activationcode")
    @Expose
    private String activationcode;

    @SerializedName("isEnabled")
    @Expose
    private boolean isEnabled;


    public User() {

    }

    public User(String id, String name, String email, String password, String phone, String img, boolean gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.img = img;
        this.gender = gender;
    }

    public User(String id, String name, String email, String password, String phone, String img, boolean gender, String creationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.img = img;
        this.gender = gender;
        this.creationDate = creationDate;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        phone = in.readString();
        img = in.readString();
        gender = in.readByte() != 0;
        creationDate = in.readString();
        notif_count = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        active = in.readString();
        assesses = in.createStringArray();
        notif_client = in.createStringArray();
        shopInvois = in.createStringArray();
        whachs = in.createStringArray();
        client_Copnes = in.createStringArray();
        favorites = in.createStringArray();
        subscriptions = in.createStringArray();
        password2 = in.readString();
        activationcode = in.readString();
        isEnabled = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getNotif_count() {
        return notif_count;
    }

    public void setNotif_count(String notif_count) {
        this.notif_count = notif_count;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String[] getAssesses() {
        return assesses;
    }

    public void setAssesses(String[] assesses) {
        this.assesses = assesses;
    }

    public String[] getNotif_client() {
        return notif_client;
    }

    public void setNotif_client(String[] notif_client) {
        this.notif_client = notif_client;
    }

    public String[] getShopInvois() {
        return shopInvois;
    }

    public void setShopInvois(String[] shopInvois) {
        this.shopInvois = shopInvois;
    }

    public String[] getWhachs() {
        return whachs;
    }

    public void setWhachs(String[] whachs) {
        this.whachs = whachs;
    }

    public String[] getClient_Copnes() {
        return client_Copnes;
    }

    public void setClient_Copnes(String[] client_Copnes) {
        this.client_Copnes = client_Copnes;
    }

    public String[] getFavorites() {
        return favorites;
    }

    public void setFavorites(String[] favorites) {
        this.favorites = favorites;
    }

    public String[] getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(String[] subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getActivationcode() {
        return activationcode;
    }

    public void setActivationcode(String activationcode) {
        this.activationcode = activationcode;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeString(img);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(creationDate);
        dest.writeString(notif_count);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(active);
        dest.writeStringArray(assesses);
        dest.writeStringArray(notif_client);
        dest.writeStringArray(shopInvois);
        dest.writeStringArray(whachs);
        dest.writeStringArray(client_Copnes);
        dest.writeStringArray(favorites);
        dest.writeStringArray(subscriptions);
        dest.writeString(password2);
        dest.writeString(activationcode);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", img='" + img + '\'' +
                ", gender=" + gender +
                ", creationDate='" + creationDate + '\'' +
                ", notif_count='" + notif_count + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", active='" + active + '\'' +
                ", assesses=" + Arrays.toString(assesses) +
                ", notif_client=" + Arrays.toString(notif_client) +
                ", shopInvois=" + Arrays.toString(shopInvois) +
                ", whachs=" + Arrays.toString(whachs) +
                ", client_Copnes=" + Arrays.toString(client_Copnes) +
                ", favorites=" + Arrays.toString(favorites) +
                ", subscriptions=" + Arrays.toString(subscriptions) +
                ", password2='" + password2 + '\'' +
                ", activationcode='" + activationcode + '\'' +
                ", isEnabled=" + isEnabled +
                '}';
    }
}