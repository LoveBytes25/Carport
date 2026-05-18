package app.util;

public class MaterialRuleUtil {

    private MaterialRuleUtil() {
    }

    public static int calculatePostQuantity(double carportLength) {

        return ((int) Math.ceil(carportLength / 300.0) + 1) * 2;
    }

    public static int calculateRafterQuantity(double carportLength) {

        return (int) Math.ceil(carportLength / 55.0);
    }

    public static int calculateRoofSheetQuantity(double carportWidth) {

        return (int) Math.ceil(carportWidth / 100.0);
    }

    public static int calculateBeamQuantity() {

        return 2;
    }
}