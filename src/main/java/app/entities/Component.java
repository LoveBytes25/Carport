package app.entities;

public class Component {

    private int id;
    private String name;
    private Double width;
    private Double height;
    private Double length;
    private String unit;
    private String description;

    public Component() {
    }

    public Component(int id, String name, Double width, Double height, Double length, String unit, String description) {

        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.unit = unit;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
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