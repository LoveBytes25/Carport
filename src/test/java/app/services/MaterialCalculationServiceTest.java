package app.services;

import app.entities.CarportComponent;
import app.exception.DatabaseException;
import app.persistence.ConnectionPool;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialCalculationServiceTest {

    private final MaterialCalculationService service =
            new MaterialCalculationService(
                    ConnectionPool.getInstance()
            );

    @Test
    void calculateShouldGenerateBom()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        assertNotNull(bom);

        assertFalse(bom.isEmpty());

        assertTrue(bom.size() >= 8);
    }

    @Test
    void calculateShouldContainPosts()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Stolpe"));

        assertTrue(found);
    }

    @Test
    void calculateShouldContainBeams()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Rem"));

        assertTrue(found);
    }

    @Test
    void calculateShouldContainRafters()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Spær"));

        assertTrue(found);
    }

    @Test
    void calculateShouldContainRoofSheets()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagplade"));

        assertTrue(found);
    }

    @Test
    void calculateShouldContainUnderStern()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        long count =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Understernbræt"))
                        .count();

        assertTrue(count >= 2);
    }

    @Test
    void calculateShouldContainOverStern()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(600, 360);

        long count =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Oversternbræt"))
                        .count();

        assertTrue(count >= 2);
    }

    @Test
    void calculateSmallCarport()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(420, 300);

        assertNotNull(bom);

        assertFalse(bom.isEmpty());

        int posts =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Stolpe"))
                        .mapToInt(CarportComponent::getQuantity)
                        .sum();

        assertEquals(6, posts);
    }

    @Test
    void calculateLargeCarport()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(780, 600);

        assertNotNull(bom);

        assertFalse(bom.isEmpty());

        int rafters =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Spær"))
                        .mapToInt(CarportComponent::getQuantity)
                        .sum();

        assertTrue(rafters >= 14);
    }

    @Test
    void optimizerShouldUseMultipleBoards()
            throws DatabaseException {

        List<CarportComponent> bom =
                service.calculate(780, 360);

        long remCount =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Rem"))
                        .count();

        assertTrue(remCount >= 2);
    }
}