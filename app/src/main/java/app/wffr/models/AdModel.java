package app.wffr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AdModel implements Parcelable {

    private String id;
    private String title;
    private String title_en;
    private String descrip;
    private String descrip_en;
    private String btn_name;
    private String btn_name_en;
    private String redirct;
    private String startDate;
    private String endDate;
    private String phon;
    private String img;
    private String active;
    private String type;
    private String place;

    public AdModel() {
    }

    public AdModel(String id, String title, String title_en, String descrip, String descrip_en, String btn_name, String btn_name_en, String redirct, String startDate, String endDate, String phon, String img, String active, String type, String place) {
        this.id = id;
        this.title = title;
        this.title_en = title_en;
        this.descrip = descrip;
        this.descrip_en = descrip_en;
        this.btn_name = btn_name;
        this.btn_name_en = btn_name_en;
        this.redirct = redirct;
        this.startDate = startDate;
        this.endDate = endDate;
        this.phon = phon;
        this.img = img;
        this.active = active;
        this.type = type;
        this.place = place;
    }

    protected AdModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        title_en = in.readString();
        descrip = in.readString();
        descrip_en = in.readString();
        btn_name = in.readString();
        btn_name_en = in.readString();
        redirct = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        phon = in.readString();
        img = in.readString();
        active = in.readString();
        type = in.readString();
        place = in.readString();
    }

    public static final Creator<AdModel> CREATOR = new Creator<AdModel>() {
        @Override
        public AdModel createFromParcel(Parcel in) {
            return new AdModel(in);
        }

        @Override
        public AdModel[] newArray(int size) {
            return new AdModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getDescrip_en() {
        return descrip_en;
    }

    public void setDescrip_en(String descrip_en) {
        this.descrip_en = descrip_en;
    }

    public String getBtn_name() {
        return btn_name;
    }

    public void setBtn_name(String btn_name) {
        this.btn_name = btn_name;
    }

    public String getBtn_name_en() {
        return btn_name_en;
    }

    public void setBtn_name_en(String btn_name_en) {
        this.btn_name_en = btn_name_en;
    }

    public String getRedirct() {
        return redirct;
    }

    public void setRedirct(String redirct) {
        this.redirct = redirct;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPhon() {
        return phon;
    }

    public void setPhon(String phon) {
        this.phon = phon;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(title_en);
        dest.writeString(descrip);
        dest.writeString(descrip_en);
        dest.writeString(btn_name);
        dest.writeString(btn_name_en);
        dest.writeString(redirct);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(phon);
        dest.writeString(img);
        dest.writeString(active);
        dest.writeString(type);
        dest.writeString(place);
    }
}