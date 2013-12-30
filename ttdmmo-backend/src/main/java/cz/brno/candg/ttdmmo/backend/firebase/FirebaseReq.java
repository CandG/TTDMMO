package cz.brno.candg.ttdmmo.backend.firebase;

/**
 *
 * @author lastuvka
 */
public class FirebaseReq {

    private int user_id;
    private String type;
    private int x;
    private int y;

    private FirebaseReq() {
    }

    public int getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getXY() {
        return "[" + this.x + ", " + this.y + "]";
    }

    @Override
    public String toString() {
        return "FirebaseReq{" + "user_id=" + user_id + ", type=" + type + ", x=" + x + ", y=" + y + '}';
    }

}
