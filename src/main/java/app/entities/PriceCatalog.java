package app.entities;

import java.time.LocalDate;

public class PriceCatalog {
    int id;
    Double unitPrice;
    LocalDate validFrom;
    LocalDate validTo;

    public PriceCatalog(int id, Double unitPrice, LocalDate validFrom, LocalDate validTo) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public int getId() {
        return id;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}
