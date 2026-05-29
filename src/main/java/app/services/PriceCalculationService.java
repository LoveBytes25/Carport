package app.services;

import app.entities.CarportComponent;
import app.entities.PriceSummary;

import app.exceptions.DatabaseException;

import app.persistence.ConnectionPool;
import app.persistence.PriceCatalogMapper;

import java.util.List;

public class PriceCalculationService {

    private static final double VAT_RATE = 0.25;

    private static final double COVERAGE_RATE = 0.39;

    private final ConnectionPool connectionPool;

    public PriceCalculationService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public PriceSummary calculatePrice(List<CarportComponent> bom) throws DatabaseException {

        double materialPrice = calculateMaterialPrice(bom);

        double salesPriceExclVat = calculateSalesPriceExclVat(materialPrice);

        double vatAmount = salesPriceExclVat * VAT_RATE;

        double totalPriceInclVat = salesPriceExclVat + vatAmount;

        PriceSummary summary = new PriceSummary();

        summary.setMaterialPrice(materialPrice);

        summary.setSalesPriceExclVat(salesPriceExclVat);

        summary.setVatAmount(vatAmount);

        summary.setTotalPriceInclVat(totalPriceInclVat);

        return summary;
    }

    private double calculateMaterialPrice(List<CarportComponent> bom) throws DatabaseException {

        double total = 0;

        for (CarportComponent row : bom) {

            int componentId = row.getComponent().getId();

            double unitPrice =
                    PriceCatalogMapper
                            .getCurrentPrice(
                                    componentId,
                                    connectionPool
                            );

            total += unitPrice * row.getQuantity();
        }

        return total;
    }

    private double calculateSalesPriceExclVat(double materialPrice) {
        return materialPrice / (1 - COVERAGE_RATE);
    }
}