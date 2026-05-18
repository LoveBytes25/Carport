package app.services;

import app.entities.CarportComponent;
import app.entities.Component;
import app.persistence.ComponentMapper;
import app.util.MaterialRuleUtil;
import app.exception.DatabaseException;
import app.persistence.ConnectionPool;

import java.util.ArrayList;
import java.util.List;

public class MaterialCalculationService {

    private final ConnectionPool connectionPool;

    public MaterialCalculationService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<CarportComponent> calculate(
            double carportLength,
            double carportWidth) throws DatabaseException {

        List<CarportComponent> bom = new ArrayList<>();

        calculatePosts(carportLength, bom);

        calculateBeams(bom);

        calculateRafters(carportLength, bom);

        calculateRoofSheets(carportWidth, bom);

        return bom;
    }

    private void calculatePosts(double carportLength, List<CarportComponent> bom)
            throws DatabaseException {

        int quantity = MaterialRuleUtil.calculatePostQuantity(carportLength);

        Component post = ComponentMapper.findComponent(
                "Stolpe",
                97,
                97,
                300,
                connectionPool
        );

        CarportComponent row = new CarportComponent();
        row.setComponent(post);
        row.setQuantity(quantity);
        row.setDescription("Stolper nedgraves 90 cm i jord");

        bom.add(row);
    }

    private void calculateBeams(List<CarportComponent> bom)
            throws DatabaseException {

        int quantity = MaterialRuleUtil.calculateBeamQuantity();

        Component beam = ComponentMapper.findComponent(
                "Rem",
                45,
                195,
                600,
                connectionPool
        );

        CarportComponent row = new CarportComponent();
        row.setComponent(beam);
        row.setQuantity(quantity);
        row.setDescription("Remme i sider");

        bom.add(row);
    }

    private void calculateRafters(double carportLength, List<CarportComponent> bom)
            throws DatabaseException {

        int quantity = MaterialRuleUtil.calculateRafterQuantity(carportLength);

        Component rafter = ComponentMapper.findComponent(
                "Spær",
                45,
                195,
                600,
                connectionPool
        );

        CarportComponent row = new CarportComponent();
        row.setComponent(rafter);
        row.setQuantity(quantity);
        row.setDescription("Spær monteres på rem");

        bom.add(row);
    }

    private void calculateRoofSheets(double carportWidth, List<CarportComponent> bom)
            throws DatabaseException {

        int quantity = MaterialRuleUtil.calculateRoofSheetQuantity(carportWidth);

        Component roofSheet = ComponentMapper.findComponent(
                "Tagplade",
                0,
                0,
                600,
                connectionPool
        );

        CarportComponent row = new CarportComponent();
        row.setComponent(roofSheet);
        row.setQuantity(quantity);
        row.setDescription("Tagplader monteres på spær");

        bom.add(row);
    }
}