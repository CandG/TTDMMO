package cz.brno.candg.ttdmmo.model;

/**
 * This entity represents rank.
 *
 * @author lastuvka
 */
public class Rank {

    private String name;
    private int people;
    private String xy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getXy() {
        return xy;
    }

    public void setXy(String xy) {
        this.xy = xy;
    }

    @Override
    public String toString() {
        return "Rank{" + "name=" + name + ", people=" + people + ", xy=" + xy + '}';
    }

}
