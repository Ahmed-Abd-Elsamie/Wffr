package app.wffr.models;

public class ExtendableMenuItem {
    private String title;
    private int img;

    public ExtendableMenuItem() {
    }

    public ExtendableMenuItem(String title, int img) {
        this.title = title;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
