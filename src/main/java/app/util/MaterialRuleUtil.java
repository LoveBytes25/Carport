package app.util;

public class MaterialRuleUtil {

    public static int calculatePosts(double length) {

        return ((int)Math.ceil(length / 300) + 1) * 2;
    }

    public static int calculateRafters(double length) {

        return (int)Math.ceil(length / 55.0);
    }

    public static int calculateBattens(double width) {

        return (int)Math.ceil(width / 35.0);
    }

    public static int calculateRoofSheets(double width) {

        return (int)Math.ceil(width / 100.0);
    }

    public static int calculateScrews(int roofSheets) {

        return roofSheets * 12;
    }
}