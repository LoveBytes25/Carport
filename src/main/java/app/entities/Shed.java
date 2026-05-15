package app.entities;

public class Shed {
    int id;
    Double length;
    Double width;

    public Shed(int id, Double length, Double width) {
        this.id = id;
        this.length = length;
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
}
