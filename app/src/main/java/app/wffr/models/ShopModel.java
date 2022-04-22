package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopModel implements Parcelable {

    private String id;
    private String name;
    private String nameen;
    private String email;
    private String password;
    private String img;
    private String discription;
    private String discriptionen;
    private String shopTypear;
    private String shopTypeen;
    private String creationDate;
    private String active;

    public ShopModel() {
    }

    public ShopModel(String id, String name, String nameen, String email, String password, String img, String discription, String discriptionen, String shopTypear, String shopTypeen, String creationDate, String active) {
        this.id = id;
        this.name = name;
        this.nameen = nameen;
        this.email = email;
        this.password = password;
        this.img = img;
        this.discription = discription;
        this.discriptionen = discriptionen;
        this.shopTypear = shopTypear;
        this.shopTypeen = shopTypeen;
        this.creationDate = creationDate;
        this.active = active;
    }

    protected ShopModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        nameen = in.readString();
        email = in.readString();
        password = in.readString();
        img = in.readString();
        discription = in.readString();
        discriptionen = in.readString();
        shopTypear = in.readString();
        shopTypeen = in.readString();
        creationDate = in.readString();
        active = in.readString();
    }

    public static final Creator<ShopModel> CREATOR = new Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(Parcel in) {
            return new ShopModel(in);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDiscriptionen() {
        return discriptionen;
    }

    public void setDiscriptionen(String discriptionen) {
        this.discriptionen = discriptionen;
    }

    public String getShopTypear() {
        return shopTypear;
    }

    public void setShopTypear(String shopTypear) {
        this.shopTypear = shopTypear;
    }

    public String getShopTypeen() {
        return shopTypeen;
    }

    public void setShopTypeen(String shopTypeen) {
        this.shopTypeen = shopTypeen;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(img);
        dest.writeString(discription);
        dest.writeString(discriptionen);
        dest.writeString(shopTypear);
        dest.writeString(shopTypeen);
        dest.writeString(creationDate);
        dest.writeString(active);
    }
}