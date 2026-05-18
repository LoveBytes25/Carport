package app.entities;

public class CarportComponent {
    private Carport carport;
    private Component component;
    private int quantity;
    private String description;

    public CarportComponent() {

    }

    public CarportComponent(Carport carport, Component component, int quantity, String description) {
        this.carport = carport;
        this.component = component;
        this.quantity = quantity;
        this.description = description;
    }

    public Carport getCarport() {
        return carport;
    }

    public void setCarport(Carport carport) {
        this.carport = carport;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
