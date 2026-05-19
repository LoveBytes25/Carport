package app.services;

import app.entities.CarportComponent;
import app.exception.DatabaseException;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialCalculationServiceTest {

    @Test
    void calculateShouldGenerateBom()
            throws DatabaseException {

        ConnectionPool connectionPool =
                ConnectionPool.getInstance();

        MaterialCalculationService service =
                new MaterialCalculationService(
                        connectionPool
                );

        List<CarportComponent> bom =
                service.calculate(600, 360);

        assertNotNull(bom);

        assertFalse(bom.isEmpty());

        assertEquals(4, bom.size());
    }

    @Test
    void calculateShouldContainPosts()
            throws DatabaseException {

        ConnectionPool connectionPool =
                ConnectionPool.getInstance();

        MaterialCalculationService service =
                new MaterialCalculationService(
                        connectionPool
                );

        List<CarportComponent> bom =
                service.calculate(600, 360);

        CarportComponent posts = bom.get(0);

        assertEquals(
                "Stolpe",
                posts.getComponent().getName()
        );

        assertEquals(
                6,
                posts.getQuantity()
        );
    }

    @Test
    void calculateShouldContainBeams()
            throws DatabaseException {

        ConnectionPool connectionPool =
                ConnectionPool.getInstance();

        MaterialCalculationService service =
                new MaterialCalculationService(
                        connectionPool
                );

        List<CarportComponent> bom =
                service.calculate(600, 360);

        CarportComponent beams = bom.get(1);

        assertEquals(
                "Rem",
                beams.getComponent().getName()
        );

        assertEquals(
                2,
                beams.getQuantity()
        );
    }

    @Test
    void calculateShouldContainRafters()
            throws DatabaseException {

        ConnectionPool connectionPool =
                ConnectionPool.getInstance();

        MaterialCalculationService service =
                new MaterialCalculationService(
                        connectionPool
                );

        List<CarportComponent> bom =
                service.calculate(600, 360);

        CarportComponent rafters = bom.get(2);

        assertEquals(
                "Spær",
                rafters.getComponent().getName()
        );

        assertEquals(
                11,
                rafters.getQuantity()
        );
    }
}