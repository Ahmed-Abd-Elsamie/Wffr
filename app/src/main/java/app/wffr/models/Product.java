package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String _id;
    private String _name;
    private String _nameen;
    private String _price;
    private String _disc;
    private boolean _showType;
    private String _codeType;
    private String _img;
    private String _creationDate;
    private String _Disciptionar;
    private String _Disciptionen;
    private String shop_name;
    private String shop_nameen;
    private String shopid;
    private String assess;
    private String watch;
    private String shopImage;
    private double price_after;

    public Product() {

    }

    public Product(String _id, String _name, String _nameen, String _price, String _disc, boolean _showType, String _codeType, String _img, String _creationDate, String _Disciptionar, String _Disciptionen, String shop_name, String shop_nameen, String shopid, String assess, String watch, String shopImage, double price_after) {
        this._id = _id;
        this._name = _name;
        this._nameen = _nameen;
        this._price = _price;
        this._disc = _disc;
        this._showType = _showType;
        this._codeType = _codeType;
        this._img = _img;
        this._creationDate = _creationDate;
        this._Disciptionar = _Disciptionar;
        this._Disciptionen = _Disciptionen;
        this.shop_name = shop_name;
        this.shop_nameen = shop_nameen;
        this.shopid = shopid;
        this.assess = assess;
        this.watch = watch;
        this.shopImage = shopImage;
        this.price_after = price_after;
    }

    protected Product(Parcel in) {
        _id = in.readString();
        _name = in.readString();
        _nameen = in.readString();
        _price = in.readString();
        _disc = in.readString();
        _showType = in.readByte() != 0;
        _codeType = in.readString();
        _img = in.readString();
        _creationDate = in.readString();
        _Disciptionar = in.readString();
        _Disciptionen = in.readString();
        shop_name = in.readString();
        shop_nameen = in.readString();
        shopid = in.readString();
        assess = in.readString();
        watch = in.readString();
        shopImage = in.readString();
        price_after = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_nameen() {
        return _nameen;
    }

    public void set_nameen(String _nameen) {
        this._nameen = _nameen;
    }

    public String get_price() {
        return _price;
    }

    public void set_price(String _price) {
        this._price = _price;
    }

    public String get_disc() {
        return _disc;
    }

    public void set_disc(String _disc) {
        this._disc = _disc;
    }

    public boolean is_showType() {
        return _showType;
    }

    public void set_showType(boolean _showType) {
        this._showType = _showType;
    }

    public String get_codeType() {
        return _codeType;
    }

    public void set_codeType(String _codeType) {
        this._codeType = _codeType;
    }

    public String get_img() {
        return _img;
    }

    public void set_img(String _img) {
        this._img = _img;
    }

    public String get_creationDate() {
        return _creationDate;
    }

    public void set_creationDate(String _creationDate) {
        this._creationDate = _creationDate;
    }

    public String get_Disciptionar() {
        return _Disciptionar;
    }

    public void set_Disciptionar(String _Disciptionar) {
        this._Disciptionar = _Disciptionar;
    }

    public String get_Disciptionen() {
        return _Disciptionen;
    }

    public void set_Disciptionen(String _Disciptionen) {
        this._Disciptionen = _Disciptionen;
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

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
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
        dest.writeString(_id);
        dest.writeString(_name);
        dest.writeString(_nameen);
        dest.writeString(_price);
        dest.writeString(_disc);
        dest.writeByte((byte) (_showType ? 1 : 0));
        dest.writeString(_codeType);
        dest.writeString(_img);
        dest.writeString(_creationDate);
        dest.writeString(_Disciptionar);
        dest.writeString(_Disciptionen);
        dest.writeString(shop_name);
        dest.writeString(shop_nameen);
        dest.writeString(shopid);
        dest.writeString(assess);
        dest.writeString(watch);
        dest.writeString(shopImage);
        dest.writeDouble(price_after);
    }
}