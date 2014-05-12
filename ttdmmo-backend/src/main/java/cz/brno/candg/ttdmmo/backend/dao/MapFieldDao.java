package cz.brno.candg.ttdmmo.backend.dao;

import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.model.MapField;

/**
 * DAO interface for Map Field
 *
 * @author Zdenek Lastuvka
 */
public interface MapFieldDao extends Dao<MapField> {

    /**
     * Get field by coord x,y
     *
     * @param x
     * @param y
     * @param valueEventListener
     */
    void getByXY(int x, int y, ValueEventListener valueEventListener);

    /**
     * Remove path from map with id
     *
     * @param id
     * @param path_id
     */
    void removePath(String id, String path_id);

    /**
     * Add path for field on x,y
     *
     * @param x
     * @param y
     * @param path_id
     */
    void addPath(int x, int y, String path_id);
}
