package cz.brno.candg.ttdmmo.constants;

/**
 * Constant for vehicle types
 *
 * @author lastuvka
 */
public class VehicleTypes {

    public static final String nothing = "N";

    private static final String[] STRINGS = {"Farm", "Wood", "Bus"};

    private static final int[] BUY_PRICE = {9000, 10000, 11000};
    private static final int[] SPEED = {3000, 2800, 2700};
    private static final int[] PRICE = {110, 65, 200};
    private static final int[] AMOUNT = {21, 31, 20};

    public static String getName(int index) {
        return STRINGS[index];
    }

    public static int namesCount() {
        return STRINGS.length;
    }

    public static int getBuyPrice(int index) {
        return BUY_PRICE[index];
    }

    public static int getSpeed(int index) {
        return SPEED[index];
    }

    public static int getPrice(int index) {
        return PRICE[index];
    }

    public static int getAmount(int index) {
        return AMOUNT[index];
    }
}
