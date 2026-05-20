package app.entities;

import java.util.List;

public class MaterialSelection {

    private List<Component> components;
    private double totalLength;
    private double waste;

    public MaterialSelection(List<Component> components, double totalLength, double waste) {

        this.components = components;
        this.totalLength = totalLength;
        this.waste = waste;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(
            List<Component> components) {

        this.components = components;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(
            double totalLength) {

        this.totalLength = totalLength;
    }

    public double getWaste() {
        return waste;
    }

    public void setWaste(double waste) {
        this.waste = waste;
    }
}