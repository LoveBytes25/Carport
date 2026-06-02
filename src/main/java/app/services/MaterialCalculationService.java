package app.services;

import app.entities.CarportComponent;
import app.entities.Component;
import app.entities.MaterialSelection;

import app.exceptions.DatabaseException;

import app.persistence.ComponentMapper;

import app.persistence.ConnectionPool;
import app.util.MaterialOptimizer;
import app.util.MaterialRuleUtil;

import java.util.ArrayList;
import java.util.List;

public class MaterialCalculationService {

    private final ConnectionPool connectionPool;

    private static final double roofTileLengthMm = 420;

    public MaterialCalculationService(ConnectionPool connectionPool) {

        this.connectionPool = connectionPool;
    }

    public List<CarportComponent> calculate(
            double carportLength,
            double carportWidth,
            String roofType,
            double roofAngle)
            throws DatabaseException {

        double carportLengthMm = carportLength * 10; // Input from frontend comes in as cm
        double carportWidthMm  = carportWidth * 10;  // Input from frontend comes in as cm

        List<CarportComponent> bom = new ArrayList<>();

        calculatePosts(carportLengthMm, bom);

        calculateBeams(carportLengthMm, bom);

        calculateRafters(
                carportLengthMm,
                carportWidthMm,
                roofType,
                roofAngle,
                bom
        );

        calculateRoof(
                carportLengthMm,
                carportWidthMm,
                roofType,
                roofAngle,
                bom
        );

        calculateUnderSternSides(carportLengthMm, bom);

        calculateUnderSternFront(carportWidthMm, bom);

        calculateOverSternSides(carportLengthMm, bom);

        calculateOverSternFront(carportWidthMm, bom);

        return bom;
    }

    private void calculatePosts(
            double carportLengthMm,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculatePostQuantity(
                        carportLengthMm
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
            double carportLengthMm,
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
                                carportLengthMm
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
            double carportLengthMm,
            double carportWidthMm,
            String roofType,
            double roofAngle,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateRafterQuantity(
                        carportLengthMm
                );

        double requiredLengthMm;

        if (roofType.equalsIgnoreCase("flat")) {

            requiredLengthMm =
                    MaterialRuleUtil.calculateFlatRafterLength(
                            carportWidthMm
                    );
        }
        else {

            requiredLengthMm =
                    MaterialRuleUtil.calculatePitchedRafterLength(
                            carportWidthMm,
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
                        requiredLengthMm
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
            double carportLengthMm,
            double carportWidthMm,
            String roofType,
            double roofAngle,
            List<CarportComponent> bom)
            throws DatabaseException {

        if (roofType.equalsIgnoreCase("flat")) {

            calculateFlatRoofSheets(
                    carportLengthMm,
                    carportWidthMm,
                    bom
            );
        }
        else {

            calculateRoofTiles(
                    carportLengthMm,
                    carportWidthMm,
                    roofAngle,
                    bom
            );
        }
    }

    private void calculateFlatRoofSheets(
            double carportLengthMm,
            double carportWidthMm,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateFlatRoofSheetQuantity(
                        carportWidthMm
                );

        List<Component> stock =
                ComponentMapper.findComponentsByName(
                        "Tagplade",
                        connectionPool
                );

        MaterialSelection selection =
                MaterialOptimizer.findBestCombination(
                        stock,
                        carportLengthMm
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
            double carportLengthMm,
            double carportWidthMm,
            double roofAngle,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateRoofTileQuantity(
                        carportLengthMm,
                        carportWidthMm,
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
                        roofTileLengthMm
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
            double carportLengthMm,
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
                        carportLengthMm
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
            double carportWidthMm,
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
                        carportWidthMm
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
            double carportLengthMm,
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
                        carportLengthMm
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
            double carportWidthMm,
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
                        carportWidthMm
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