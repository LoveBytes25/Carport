package app.entities;

public class PriceSummary {

    private double materialPrice;

    private double salesPriceExclVat;

    private double vatAmount;

    private double totalPriceInclVat;

    public double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public double getSalesPriceExclVat() {
        return salesPriceExclVat;
    }

    public void setSalesPriceExclVat(double salesPriceExclVat) {
        this.salesPriceExclVat = salesPriceExclVat;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public double getTotalPriceInclVat() {
        return totalPriceInclVat;
    }

    public void setTotalPriceInclVat(double totalPriceInclVat) {
        this.totalPriceInclVat = totalPriceInclVat;
    }
}