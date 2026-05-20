package app.util;

public class MaterialRuleUtil {

    private MaterialRuleUtil() {
    }

    public static int calculatePostQuantity(double carportLength) {
        return ((int) Math.ceil(carportLength / 3000.0) + 1) * 2;
    }

    public static double calculatePostLength() {
        return 3000;
    }

    public static int calculateBeamQuantity() {
        return 2;
    }

    public static double calculateBeamLength(double carportLength) {
        return carportLength;
    }

    public static int calculateRafterQuantity(double carportLength) {
        return (int) Math.ceil(carportLength / 550.0);
    }

    public static double calculateFlatRafterLength(double carportWidth) {
        return carportWidth;
    }

    public static double calculatePitchedRafterLength(double carportWidth, double roofAngleDegrees) {

        double radians =
                Math.toRadians(
                        roofAngleDegrees
                );

        return carportWidth /
                Math.cos(radians);
    }

    public static int calculateRoofSheetQuantity(double carportWidth) {
        return (int) Math.ceil(carportWidth / 1000.0);
    }

    public static double calculateRoofSheetLength(double carportLength) {
        return carportLength;
    }

    public static int calculateSideBoardQuantity() {

        return 2;
    }

    public static int calculateFrontBoardQuantity() {

        return 2;
    }
}