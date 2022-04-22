package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationItem implements Parcelable {

    private String id;
    private String titlear;
    private String titleen;
    private String discriptionar;
    private String discriptionen;
    private String gender;
    private String img;
    private String redirct;
    private String emoticons;
    private String[] notif_client;
    private int product_id;


    public NotificationItem() {

    }

    public NotificationItem(String id, String titlear, String titleen, String discriptionar, String discriptionen, String gender, String img, String redirct, String emoticons, String[] notif_client, int product_id) {
        this.id = id;
        this.titlear = titlear;
        this.titleen = titleen;
        this.discriptionar = discriptionar;
        this.discriptionen = discriptionen;
        this.gender = gender;
        this.img = img;
        this.redirct = redirct;
        this.emoticons = emoticons;
        this.notif_client = notif_client;
        this.product_id = product_id;
    }

    protected NotificationItem(Parcel in) {
        id = in.readString();
        titlear = in.readString();
        titleen = in.readString();
        discriptionar = in.readString();
        discriptionen = in.readString();
        gender = in.readString();
        img = in.readString();
        redirct = in.readString();
        emoticons = in.readString();
        notif_client = in.createStringArray();
        product_id = in.readInt();
    }

    public static final Creator<NotificationItem> CREATOR = new Creator<NotificationItem>() {
        @Override
        public NotificationItem createFromParcel(Parcel in) {
            return new NotificationItem(in);
        }

        @Override
        public NotificationItem[] newArray(int size) {
            return new NotificationItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitlear() {
        return titlear;
    }

    public void setTitlear(String titlear) {
        this.titlear = titlear;
    }

    public String getTitleen() {
        return titleen;
    }

    public void setTitleen(String titleen) {
        this.titleen = titleen;
    }

    public String getDiscriptionar() {
        return discriptionar;
    }

    public void setDiscriptionar(String discriptionar) {
        this.discriptionar = discriptionar;
    }

    public String getDiscriptionen() {
        return discriptionen;
    }

    public void setDiscriptionen(String discriptionen) {
        this.discriptionen = discriptionen;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRedirct() {
        return redirct;
    }

    public void setRedirct(String redirct) {
        this.redirct = redirct;
    }

    public String getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(String emoticons) {
        this.emoticons = emoticons;
    }

    public String[] getNotif_client() {
        return notif_client;
    }

    public void setNotif_client(String[] notif_client) {
        this.notif_client = notif_client;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titlear);
        dest.writeString(titleen);
        dest.writeString(discriptionar);
        dest.writeString(discriptionen);
        dest.writeString(gender);
        dest.writeString(img);
        dest.writeString(redirct);
        dest.writeString(emoticons);
        dest.writeStringArray(notif_client);
        dest.writeInt(product_id);
    }
}