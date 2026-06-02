package app.entities;

import java.time.LocalDateTime;

public class Carport {
    private int id;
    private Double height;
    private Double width;
    private Double length;
    private Double roofAngle;
    private LocalDateTime timestamp;

    public Carport(int id, Double height, Double width, Double length, Double roofAngle, LocalDateTime timestamp) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.length = length;
        this.roofAngle = roofAngle;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getRoofAngle() {
        return roofAngle;
    }

    public void setRoofAngle() {
        this.roofAngle = roofAngle;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
