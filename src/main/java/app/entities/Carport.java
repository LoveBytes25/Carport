package app.entities;

import java.time.LocalDateTime;

public class Carport {
    int id;
    Double height;
    Double width;
    Double length;
    LocalDateTime timestamp;

    public Carport(int id, Double height, Double width, Double length, LocalDateTime timestamp) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.length = length;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
