package cz.brno.candg.ttdmmo.constants;

/**
 * Constants for Firebase and the other settings
 *
 * @author lastuvka
 */
public interface FbRef {

    public static final String ref = "https://ttdmmoq.firebaseio.com/";
    public static final String refD = "https://ttdmmoq.firebaseio.com/data/";
    public static final String refS = "https://ttdmmoq.firebaseio.com/server/";
    public static final String refC = "https://ttdmmoq.firebaseio.com/const/";
    public static final String refQ = "https://ttdmmoq.firebaseio.com/queue/";
    public static final int PROCCESING_DELAY = 300;
    public static final int SYNC_INTERVAL = 3;
    public static final int CITY_PADDING = 20;
    public static final int MAX_SERVERS = 1000;
    public static final int START_PEOPLE = 50;
    public static final int START_MONEY = 50000;
    public static final String START_COLOR = "#1d81f7";
}
