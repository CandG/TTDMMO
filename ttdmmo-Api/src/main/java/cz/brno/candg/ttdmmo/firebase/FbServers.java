package cz.brno.candg.ttdmmo.firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO for list of servers
 *
 * @author lastuvka
 */
public class FbServers {

    private final Map<String, Boolean> list = new HashMap<String, Boolean>();

    public Map<String, Boolean> getList() {
        return list;
    }

}
