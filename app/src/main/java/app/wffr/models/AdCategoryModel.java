package app.wffr.models;

public class AdCategoryModel {

    private int id;
    private String image;
    private String title_ar;
    private String title_en;
    private String active;
    private String category_id;
    private String fromData;
    private String toData;
    private String shopCategory;

    public AdCategoryModel() {
    }

    public AdCategoryModel(int id, String image, String title_ar, String title_en, String active, String category_id, String fromData, String toData, String shopCategory) {
        this.id = id;
        this.image = image;
        this.title_ar = title_ar;
        this.title_en = title_en;
        this.active = active;
        this.category_id = category_id;
        this.fromData = fromData;
        this.toData = toData;
        this.shopCategory = shopCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getFromData() {
        return fromData;
    }

    public void setFromData(String fromData) {
        this.fromData = fromData;
    }

    public String getToData() {
        return toData;
    }

    public void setToData(String toData) {
        this.toData = toData;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }


}