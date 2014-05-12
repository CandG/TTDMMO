package cz.brno.candg.ttdmmo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This entity represents path.
 *
 * @author lastuvka
 */
public class Path {

    private List<String> path = new ArrayList<String>();
    private transient String id;

    public String transientGetId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Path{" + "path=" + path + ", id=" + id + '}';
    }

}
