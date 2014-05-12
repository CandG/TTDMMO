package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.ConstDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO impl. for constants
 *
 * @author lastuvka
 */
public class ConstDaoFbImpl implements ConstDao {

    final static Logger log = LoggerFactory.getLogger(ConstDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.refC);
    private final Firebase refAll = new Firebase(FbRef.ref);

    @Override
    public void setUSER_NUM_OFFSET(int value) {
        Firebase childRef = ref.child("USER_NUM_OFFSET");
        childRef.setValue(value);
    }

    @Override
    public void removeAll() {
        refAll.setValue(null);
    }
}
