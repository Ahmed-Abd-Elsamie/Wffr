package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable {

    private String id;
    private String name;
    private String nameen;
    private String email;
    private String password;
    private String img;
    private String discription;
    private String discriptionen;
    private String shopType;
    private String creationDate;
    private String active;
    private String imgCover;
    private String[] assesses;
    private String[] branches;
    private String[] client_Copnes;
    private String[] products;
    private String[] shopInvois;
    private String[] users_shop;
    private String shopCategory;
    private String password2;

    public Shop() {
    }

    public Shop(String id, String name, String nameen, String email, String password, String img, String discription, String discriptionen, String shopType, String creationDate, String active, String imgCover, String[] assesses, String[] branches, String[] client_Copnes, String[] products, String[] shopInvois, String[] users_shop, String shopCategory, String password2) {
        this.id = id;
        this.name = name;
        this.nameen = nameen;
        this.email = email;
        this.password = password;
        this.img = img;
        this.discription = discription;
        this.discriptionen = discriptionen;
        this.shopType = shopType;
        this.creationDate = creationDate;
        this.active = active;
        this.imgCover = imgCover;
        this.assesses = assesses;
        this.branches = branches;
        this.client_Copnes = client_Copnes;
        this.products = products;
        this.shopInvois = shopInvois;
        this.users_shop = users_shop;
        this.shopCategory = shopCategory;
        this.password2 = password2;
    }

    protected Shop(Parcel in) {
        id = in.readString();
        name = in.readString();
        nameen = in.readString();
        email = in.readString();
        password = in.readString();
        img = in.readString();
        discription = in.readString();
        discriptionen = in.readString();
        shopType = in.readString();
        creationDate = in.readString();
        active = in.readString();
        imgCover = in.readString();
        assesses = in.createStringArray();
        branches = in.createStringArray();
        client_Copnes = in.createStringArray();
        products = in.createStringArray();
        shopInvois = in.createStringArray();
        users_shop = in.createStringArray();
        shopCategory = in.readString();
        password2 = in.readString();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
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

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
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

    public String getImgCover() {
        return imgCover;
    }

    public void setImgCover(String imgCover) {
        this.imgCover = imgCover;
    }

    public String[] getAssesses() {
        return assesses;
    }

    public void setAssesses(String[] assesses) {
        this.assesses = assesses;
    }

    public String[] getBranches() {
        return branches;
    }

    public void setBranches(String[] branches) {
        this.branches = branches;
    }

    public String[] getClient_Copnes() {
        return client_Copnes;
    }

    public void setClient_Copnes(String[] client_Copnes) {
        this.client_Copnes = client_Copnes;
    }

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public String[] getShopInvois() {
        return shopInvois;
    }

    public void setShopInvois(String[] shopInvois) {
        this.shopInvois = shopInvois;
    }

    public String[] getUsers_shop() {
        return users_shop;
    }

    public void setUsers_shop(String[] users_shop) {
        this.users_shop = users_shop;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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
        dest.writeString(shopType);
        dest.writeString(creationDate);
        dest.writeString(active);
        dest.writeString(imgCover);
        dest.writeStringArray(assesses);
        dest.writeStringArray(branches);
        dest.writeStringArray(client_Copnes);
        dest.writeStringArray(products);
        dest.writeStringArray(shopInvois);
        dest.writeStringArray(users_shop);
        dest.writeString(shopCategory);
        dest.writeString(password2);
    }
}