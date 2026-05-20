package app;

import app.entities.CarportComponent;
import app.exception.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.MaterialCalculationService;

import java.util.List;

public class Main
{
    public static void main(String[] args)  throws DatabaseException {
        ConnectionPool connectionPool =
                ConnectionPool.getInstance();

        MaterialCalculationService service =
                new MaterialCalculationService(
                        connectionPool
                );

        List<CarportComponent> bom =
                service.calculate(
                        7200,
                        4200,
                        "angled",
                        25
                );

        System.out.println();
        System.out.println("===== STYKLISTE =====");
        System.out.println();

        for (CarportComponent row : bom) {

            System.out.println(
                    row.getQuantity()
                            + "x "
                            + row.getComponent().getName()
                            + " | "
                            + row.getComponent().getWidth()
                            + "x"
                            + row.getComponent().getHeight()
                            + " mm"
                            + " | længde: "
                            + row.getComponent().getLength()
                            + " mm"
                            + " | "
                            + row.getDescription()
            );
        }

        System.out.println();
        System.out.println("===== SLUT =====");
    }
}