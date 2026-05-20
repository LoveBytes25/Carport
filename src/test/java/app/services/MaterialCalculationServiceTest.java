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

        assertEquals(
                300,
                posts.getComponent().getLength()
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

        assertTrue(
                beams.getComponent().getLength() >= 600
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

        assertTrue(
                rafters.getComponent().getLength() >= 360
        );
    }

    @Test
    void calculateShouldContainRoofSheets()
            throws DatabaseException {

        ConnectionPool connectionPool =
                ConnectionPool.getInstance();

        MaterialCalculationService service =
                new MaterialCalculationService(
                        connectionPool
                );

        List<CarportComponent> bom =
                service.calculate(600, 360);

        CarportComponent roofSheets = bom.get(3);

        assertEquals(
                "Tagplade",
                roofSheets.getComponent().getName()
        );

        assertEquals(
                4,
                roofSheets.getQuantity()
        );

        assertTrue(
                roofSheets.getComponent().getLength() >= 600
        );
    }
}