package app.services;

import app.entities.CarportComponent;
import app.entities.Component;
import app.entities.MaterialSelection;

import app.exceptions.DatabaseException;

import app.persistence.ComponentMapper;
import app.config.ConnectionPool;

import app.util.MaterialOptimizer;
import app.util.MaterialRuleUtil;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialCalculationServiceTest {

    @Test
    void calculateFlatRoofShouldGenerateBom()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        assertNotNull(bom);

        assertFalse(bom.isEmpty());
    }

    @Test
    void calculatePitchedRoofShouldGenerateBom()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        7200,
                        4200,
                        "angled",
                        25
                );

        assertNotNull(bom);

        assertFalse(bom.isEmpty());
    }

    @Test
    void calculateShouldContainPosts()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        assertEquals(
                "Stolpe",
                bom.get(0).getComponent().getName()
        );
    }

    @Test
    void calculateShouldContainBeams()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        assertEquals(
                "Rem",
                bom.get(1).getComponent().getName()
        );
    }

    @Test
    void calculateShouldContainRafters()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        assertEquals(
                "Spær",
                bom.get(2).getComponent().getName()
        );
    }

    @Test
    void calculateFlatRoofShouldContainRoofSheets()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagplade"));

        assertTrue(found);
    }

    @Test
    void calculatePitchedRoofShouldContainTiles()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        7200,
                        4200,
                        "angled",
                        25
                );

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagsten"));

        assertTrue(found);
    }

    @Test
    void calculateShouldContainUnderStern()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Understernbræt"));

        assertTrue(found);
    }

    @Test
    void calculateShouldContainOverStern()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        boolean found =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Oversternbræt"));

        assertTrue(found);
    }

    @Test
    void flatRoofShouldCalculateCorrectRafterQuantity()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        CarportComponent rafters =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Spær"))
                        .findFirst()
                        .orElse(null);

        assertNotNull(rafters);

        assertEquals(
                11,
                rafters.getQuantity()
        );
    }

    @Test
    void angledRoofShouldCalculateCorrectRafterQuantity()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        7200,
                        4200,
                        "angled",
                        25
                );

        CarportComponent rafters =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Spær"))
                        .findFirst()
                        .orElse(null);

        assertNotNull(rafters);

        assertEquals(
                14,
                rafters.getQuantity()
        );
    }

    @Test
    void beamShouldChooseClosestPossibleLength()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        5400,
                        3600,
                        "flat",
                        0
                );

        CarportComponent beam =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Rem"))
                        .findFirst()
                        .orElse(null);

        assertNotNull(beam);

        assertEquals(
                6000,
                beam.getComponent().getLength()
        );
    }

    @Test
    void pitchedRoofShouldCalculateLongerRafters() {

        double length =
                MaterialRuleUtil
                        .calculatePitchedRafterLength(
                                3600,
                                25
                        );

        assertTrue(length > 3600);
    }

    @Test
    void pitchedRoofShouldCalculateCorrectLength() {

        double result =
                MaterialRuleUtil
                        .calculatePitchedRafterLength(
                                3600,
                                25
                        );

        assertEquals(
                3972,
                Math.round(result)
        );
    }

    @Test
    void flatRoofShouldUseTagplader()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        6000,
                        3600,
                        "flat",
                        0
                );

        boolean hasSheets =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagplade"));

        boolean hasTiles =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagsten"));

        assertTrue(hasSheets);

        assertFalse(hasTiles);
    }

    @Test
    void angledRoofShouldUseTagsten()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        7200,
                        4200,
                        "angled",
                        25
                );

        boolean hasSheets =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagplade"));

        boolean hasTiles =
                bom.stream()
                        .anyMatch(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Tagsten"));

        assertFalse(hasSheets);

        assertTrue(hasTiles);
    }

    @Test
    void optimizerShouldChooseLeastWaste()
            throws DatabaseException {

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Rem",
                        ConnectionPool.getInstance()
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        5400
                );

        assertEquals(
                6000,
                selection.getComponents()
                        .get(0)
                        .getLength()
        );
    }

    @Test
    void largerCarportShouldRequireMorePosts()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        7800,
                        4200,
                        "flat",
                        0
                );

        CarportComponent posts =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Stolpe"))
                        .findFirst()
                        .orElse(null);

        assertNotNull(posts);

        assertEquals(
                8,
                posts.getQuantity()
        );
    }

    @Test
    void largerCarportShouldRequireMoreRafters()
            throws DatabaseException {

        List<CarportComponent> bom =
                new MaterialCalculationService(
                        ConnectionPool.getInstance()
                ).calculate(
                        7800,
                        4200,
                        "flat",
                        0
                );

        CarportComponent rafters =
                bom.stream()
                        .filter(c ->
                                c.getComponent()
                                        .getName()
                                        .equals("Spær"))
                        .findFirst()
                        .orElse(null);

        assertNotNull(rafters);

        assertEquals(
                15,
                rafters.getQuantity()
        );
    }
}