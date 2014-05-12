package cz.brno.candg.ttdmmo.backend.dao;

/**
 * DAO interface for constants
 *
 * @author lastuvka
 */
public interface ConstDao {

    /**
     * Set constant for offset of user ID in Firebase
     *
     * @param value
     */
    void setUSER_NUM_OFFSET(int value);

    /**
     * Remove all data - for new game
     */
    void removeAll();
}
