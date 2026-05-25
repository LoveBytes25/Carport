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

    public static double calculatePitchedRafterLength(double carportWidth, double roofAngle) {

        double halfWidth = carportWidth / 2.0;

        double radians = Math.toRadians(roofAngle);

        return (halfWidth / Math.cos(radians)) * 2;
    }

    public static int calculateFlatRoofSheetQuantity(double carportWidth) {

        return (int) Math.ceil(carportWidth / 1000.0);
    }

    public static int calculateTileRows(double carportLength, double roofAngle) {

        double spacing;

        if (roofAngle < 22) {

            spacing = 312;
        }
        else {

            spacing = 370;
        }

        return (int) Math.ceil(carportLength / spacing);
    }

    public static int calculateTilesPerRow(double carportWidth) {

        return (int) Math.ceil(carportWidth / 299.0);
    }

    public static int calculateRoofTileQuantity(double carportLength, double carportWidth, double roofAngle) {

        return calculateTileRows(carportLength, roofAngle)
                * calculateTilesPerRow(carportWidth)
                * 2;
    }

    public static int calculateSideBoardQuantity() {

        return 2;
    }

    public static int calculateFrontBoardQuantity() {

        return 2;
    }
}