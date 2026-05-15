package app.entities;

public class CarportComponent {
    int quantity;
    Double cutLength;
    String description;

    public CarportComponent(int quantity, Double cutLength, String description) {
        this.quantity = quantity;
        this.cutLength = cutLength;
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getCutLength() {
        return cutLength;
    }

    public void setCutLength(Double cutLength) {
        this.cutLength = cutLength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
