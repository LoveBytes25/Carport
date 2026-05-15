package app.services;

import app.entities.CarportComponent;
import app.util.MaterialRuleUtil;

import java.util.ArrayList;
import java.util.List;

public class MaterialCalculationService {

    public List<CarportComponent> calculate(
            double length,
            double width) {

        List<CarportComponent> bom =
                new ArrayList<>();

        int posts = MaterialRuleUtil.calculatePosts(length);

        int rafters = MaterialRuleUtil.calculateRafters(length);

        // create BOM (bill of materials) rows

        return bom;
    }
}
