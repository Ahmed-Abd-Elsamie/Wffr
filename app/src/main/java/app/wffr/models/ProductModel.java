package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel implements Parcelable {

    private String id;
    private String name;
    private String nameen;
    private String price;
    private String disc;
    private boolean showType;
    private String codeType;
    private String img;
    private String creationDate;
    private String disciptionar;
    private String disciptionen;
    private String shop_name;
    private String shop_nameen;
    private String shopid;
    private String assess;
    private String watch;

    @Expose
    @SerializedName("product_expair")
    private String product_expire;

    private boolean isfav;
    private double price_after;

    public ProductModel() {

    }

    public ProductModel(String id, String name, String nameen, String price, String disc, boolean showType, String codeType, String img, String creationDate, String disciptionar, String disciptionen, String shop_name, String shop_nameen, String shopid, String assess, String watch, String product_expire, boolean isfav, double price_after) {
        this.id = id;
        this.name = name;
        this.nameen = nameen;
        this.price = price;
        this.disc = disc;
        this.showType = showType;
        this.codeType = codeType;
        this.img = img;
        this.creationDate = creationDate;
        this.disciptionar = disciptionar;
        this.disciptionen = disciptionen;
        this.shop_name = shop_name;
        this.shop_nameen = shop_nameen;
        this.shopid = shopid;
        this.assess = assess;
        this.watch = watch;
        this.product_expire = product_expire;
        this.isfav = isfav;
        this.price_after = price_after;
    }

    protected ProductModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        nameen = in.readString();
        price = in.readString();
        disc = in.readString();
        showType = in.readByte() != 0;
        codeType = in.readString();
        img = in.readString();
        creationDate = in.readString();
        disciptionar = in.readString();
        disciptionen = in.readString();
        shop_name = in.readString();
        shop_nameen = in.readString();
        shopid = in.readString();
        assess = in.readString();
        watch = in.readString();
        product_expire = in.readString();
        isfav = in.readByte() != 0;
        price_after = in.readDouble();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public boolean isShowType() {
        return showType;
    }

    public void setShowType(boolean showType) {
        this.showType = showType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDisciptionar() {
        return disciptionar;
    }

    public void setDisciptionar(String disciptionar) {
        this.disciptionar = disciptionar;
    }

    public String getDisciptionen() {
        return disciptionen;
    }

    public void setDisciptionen(String disciptionen) {
        this.disciptionen = disciptionen;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_nameen() {
        return shop_nameen;
    }

    public void setShop_nameen(String shop_nameen) {
        this.shop_nameen = shop_nameen;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getAssess() {
        return assess;
    }

    public void setAssess(String assess) {
        this.assess = assess;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    public String getProduct_expire() {
        return product_expire;
    }

    public void setProduct_expire(String product_expire) {
        this.product_expire = product_expire;
    }

    public boolean isIsfav() {
        return isfav;
    }

    public void setIsfav(boolean isfav) {
        this.isfav = isfav;
    }

    public double getPrice_after() {
        return price_after;
    }

    public void setPrice_after(double price_after) {
        this.price_after = price_after;
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
        dest.writeString(price);
        dest.writeString(disc);
        dest.writeByte((byte) (showType ? 1 : 0));
        dest.writeString(codeType);
        dest.writeString(img);
        dest.writeString(creationDate);
        dest.writeString(disciptionar);
        dest.writeString(disciptionen);
        dest.writeString(shop_name);
        dest.writeString(shop_nameen);
        dest.writeString(shopid);
        dest.writeString(assess);
        dest.writeString(watch);
        dest.writeString(product_expire);
        dest.writeByte((byte) (isfav ? 1 : 0));
        dest.writeDouble(price_after);
    }
}