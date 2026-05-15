package app.entities;

public class Component {
    int id;
    String name;
    Double width;
    Double height;
    String unit;
    String description;

    public Component(int id, String name, Double width, Double height, String unit, String description) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.unit = unit;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
