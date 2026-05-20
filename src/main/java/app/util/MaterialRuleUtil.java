package app.util;

public class MaterialRuleUtil {

    private MaterialRuleUtil() {
    }

    public static int calculatePostQuantity(double carportLength) {

        return ((int) Math.ceil(carportLength / 300.0) + 1) * 2;
    }

    public static double calculatePostLength() {

        return 300;
    }

    public static int calculateBeamQuantity() {

        return 2;
    }

    public static double calculateBeamLength(double carportLength) {

        return carportLength;
    }

    public static int calculateRafterQuantity(double carportLength) {

        return (int) Math.ceil(carportLength / 55.0);
    }

    public static double calculateRafterLength(double carportWidth) {

        return carportWidth;
    }

    public static int calculateRoofSheetQuantity(double carportWidth) {

        return (int) Math.ceil(carportWidth / 100.0);
    }

    public static double calculateRoofSheetLength(double carportLength) {

        return carportLength;
    }
}