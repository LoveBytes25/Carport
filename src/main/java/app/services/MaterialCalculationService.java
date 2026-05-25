package app.services;

import app.entities.CarportComponent;
import app.entities.Component;
import app.entities.MaterialSelection;

import app.exception.DatabaseException;

import app.persistence.ComponentMapper;
import app.persistence.ConnectionPool;

import app.util.MaterialOptimizer;
import app.util.MaterialRuleUtil;

import java.util.ArrayList;
import java.util.List;

public class MaterialCalculationService {

    private final ConnectionPool connectionPool;

    public MaterialCalculationService(ConnectionPool connectionPool) {

        this.connectionPool = connectionPool;
    }

    public List<CarportComponent> calculate(
            double carportLength,
            double carportWidth,
            String roofType,
            double roofAngle)
            throws DatabaseException {

        List<CarportComponent> bom = new ArrayList<>();

        calculatePosts(carportLength, bom);

        calculateBeams(carportLength, bom);

        calculateRafters(
                carportLength,
                carportWidth,
                roofType,
                roofAngle,
                bom
        );

        calculateRoof(
                carportLength,
                carportWidth,
                roofType,
                roofAngle,
                bom
        );

        calculateUnderSternSides(carportLength, bom);

        calculateUnderSternFront(carportWidth, bom);

        calculateOverSternSides(carportLength, bom);

        calculateOverSternFront(carportWidth, bom);

        return bom;
    }

    private void calculatePosts(
            double carportLength,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculatePostQuantity(
                        carportLength
                );

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Stolpe",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        MaterialRuleUtil.calculatePostLength()
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(quantity);

            row.setDescription(
                    "Stolper nedgraves 90 cm i jord"
            );

            bom.add(row);
        }
    }

    private void calculateBeams(
            double carportLength,
            List<CarportComponent> bom)
            throws DatabaseException {

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Rem",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        MaterialRuleUtil.calculateBeamLength(
                                carportLength
                        )
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(
                    MaterialRuleUtil.calculateBeamQuantity()
            );

            row.setDescription(
                    "Remme i sider"
            );

            bom.add(row);
        }
    }

    private void calculateRafters(
            double carportLength,
            double carportWidth,
            String roofType,
            double roofAngle,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateRafterQuantity(
                        carportLength
                );

        double requiredLength;

        if (roofType.equalsIgnoreCase("flat")) {

            requiredLength =
                    MaterialRuleUtil.calculateFlatRafterLength(
                            carportWidth
                    );
        }
        else {

            requiredLength =
                    MaterialRuleUtil.calculatePitchedRafterLength(
                            carportWidth,
                            roofAngle
                    );
        }

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Spær",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        requiredLength
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(quantity);

            row.setDescription(
                    "Spær monteres på rem"
            );

            bom.add(row);
        }
    }

    private void calculateRoof(
            double carportLength,
            double carportWidth,
            String roofType,
            double roofAngle,
            List<CarportComponent> bom)
            throws DatabaseException {

        if (roofType.equalsIgnoreCase("flat")) {

            calculateFlatRoofSheets(
                    carportLength,
                    carportWidth,
                    bom
            );
        }
        else {

            calculateRoofTiles(
                    carportLength,
                    carportWidth,
                    roofAngle,
                    bom
            );
        }
    }

    private void calculateFlatRoofSheets(
            double carportLength,
            double carportWidth,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateFlatRoofSheetQuantity(
                        carportWidth
                );

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Tagplade",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        carportLength
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(quantity);

            row.setDescription(
                    "Tagplader monteres på spær"
            );

            bom.add(row);
        }
    }

    private void calculateRoofTiles(
            double carportLength,
            double carportWidth,
            double roofAngle,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateRoofTileQuantity(
                        carportLength,
                        carportWidth,
                        roofAngle
                );

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Tagsten",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        420
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(quantity);

            row.setDescription(
                    "Tagsten til sadeltag"
            );

            bom.add(row);
        }
    }

    private void calculateUnderSternSides(
            double carportLength,
            List<CarportComponent> bom)
            throws DatabaseException {

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Understernbræt",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        carportLength
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(2);

            row.setDescription(
                    "Understernbrædder til sider"
            );

            bom.add(row);
        }
    }

    private void calculateUnderSternFront(
            double carportWidth,
            List<CarportComponent> bom)
            throws DatabaseException {

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Understernbræt",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        carportWidth
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(2);

            row.setDescription(
                    "Understernbrædder til forende"
            );

            bom.add(row);
        }
    }

    private void calculateOverSternSides(
            double carportLength,
            List<CarportComponent> bom)
            throws DatabaseException {

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Oversternbræt",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        carportLength
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(2);

            row.setDescription(
                    "Oversternbrædder til sider"
            );

            bom.add(row);
        }
    }

    private void calculateOverSternFront(
            double carportWidth,
            List<CarportComponent> bom)
            throws DatabaseException {

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Oversternbræt",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        carportWidth
                );

        for (Component component : selection.getComponents()) {

            CarportComponent row = new CarportComponent();

            row.setComponent(component);

            row.setQuantity(2);

            row.setDescription(
                    "Oversternbrædder til forende"
            );

            bom.add(row);
        }
    }
}