package app.services;

import app.entities.CarportComponent;
import app.entities.Component;
import app.exceptions.DatabaseException;
import app.config.ConnectionPool;
import org.junit.jupiter.api.Test;

import java.util.List;

class MaterialCalculationServiceTestOfBomPrint {

    @Test
    void printBomForFlatRoof() throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        System.out.println("\n===== FLAT ROOF BOM =====");

        for (CarportComponent row : bom) {

            Component component = row.getComponent();

            System.out.println(
                    row.getQuantity()
                            + "x "
                            + component.getName()
                            + " "
                            + component.getWidth().intValue()
                            + "x"
                            + component.getHeight().intValue()
                            + " "
                            + component.getLength().intValue()
                            + " mm"
                            + " | "
                            + row.getDescription()
            );
        }
    }

    @Test
    void printBomForAngledRoof() throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "angled",
                        25
                );

        System.out.println("\n===== ANGLED ROOF BOM =====");

        for (CarportComponent row : bom) {

            Component component = row.getComponent();

            System.out.println(
                    row.getQuantity()
                            + "x "
                            + component.getName()
                            + " "
                            + component.getWidth().intValue()
                            + "x"
                            + component.getHeight().intValue()
                            + " "
                            + component.getLength().intValue()
                            + " mm"
                            + " | "
                            + row.getDescription()
            );
        }
    }

}