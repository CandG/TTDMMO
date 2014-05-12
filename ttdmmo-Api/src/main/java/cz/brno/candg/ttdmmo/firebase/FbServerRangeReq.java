package cz.brno.candg.ttdmmo.firebase;

/**
 * POJO for range of server
 *
 * @author lastuvka
 */
public class FbServerRangeReq {

    private int start;
    private int end;

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

}
