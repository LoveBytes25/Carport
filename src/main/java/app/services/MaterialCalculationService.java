package app.services;

import app.entities.CarportComponent;
import app.entities.Component;
import app.exception.DatabaseException;
import app.persistence.ComponentMapper;
import app.persistence.ConnectionPool;
import app.util.MaterialRuleUtil;

import java.util.ArrayList;
import java.util.List;

public class MaterialCalculationService {

    private final ConnectionPool connectionPool;

    public MaterialCalculationService(
            ConnectionPool connectionPool) {

        this.connectionPool = connectionPool;
    }

    public List<CarportComponent> calculate(
            double carportLength,
            double carportWidth)
            throws DatabaseException {

        List<CarportComponent> bom = new ArrayList<>();

        calculatePosts(carportLength, bom);

        calculateBeams(carportLength, bom);

        calculateRafters(
                carportLength,
                carportWidth,
                bom
        );

        calculateRoofSheets(
                carportLength,
                carportWidth,
                bom
        );

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

        double requiredLength =
                MaterialRuleUtil.calculatePostLength();

        Component post =
                ComponentMapper.findBestComponent(
                        "Stolpe",
                        requiredLength,
                        connectionPool
                );

        CarportComponent row =
                new CarportComponent();

        row.setComponent(post);
        row.setQuantity(quantity);
        row.setDescription(
                "Stolper nedgraves 90 cm i jord"
        );

        bom.add(row);
    }

    private void calculateBeams(
            double carportLength,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateBeamQuantity();

        double requiredLength =
                MaterialRuleUtil.calculateBeamLength(
                        carportLength
                );

        Component beam =
                ComponentMapper.findBestComponent(
                        "Rem",
                        requiredLength,
                        connectionPool
                );

        CarportComponent row =
                new CarportComponent();

        row.setComponent(beam);
        row.setQuantity(quantity);
        row.setDescription(
                "Remme i sider"
        );

        bom.add(row);
    }

    private void calculateRafters(
            double carportLength,
            double carportWidth,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateRafterQuantity(
                        carportLength
                );

        double requiredLength =
                MaterialRuleUtil.calculateRafterLength(
                        carportWidth
                );

        Component rafter =
                ComponentMapper.findBestComponent(
                        "Spær",
                        requiredLength,
                        connectionPool
                );

        CarportComponent row =
                new CarportComponent();

        row.setComponent(rafter);
        row.setQuantity(quantity);
        row.setDescription(
                "Spær monteres på rem"
        );

        bom.add(row);
    }

    private void calculateRoofSheets(
            double carportLength,
            double carportWidth,
            List<CarportComponent> bom)
            throws DatabaseException {

        int quantity =
                MaterialRuleUtil.calculateRoofSheetQuantity(
                        carportWidth
                );

        double requiredLength =
                MaterialRuleUtil.calculateRoofSheetLength(
                        carportLength
                );

        Component roofSheet =
                ComponentMapper.findBestComponent(
                        "Tagplade",
                        requiredLength,
                        connectionPool
                );

        CarportComponent row =
                new CarportComponent();

        row.setComponent(roofSheet);
        row.setQuantity(quantity);
        row.setDescription(
                "Tagplader monteres på spær"
        );

        bom.add(row);
    }
}