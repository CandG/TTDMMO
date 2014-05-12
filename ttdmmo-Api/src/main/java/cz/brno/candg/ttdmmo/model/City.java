package cz.brno.candg.ttdmmo.model;

/**
 * This entity represents city.
 *
 * @author lastuvka
 */
public class City {

    private String name;
    private int people;
    private int food;
    private int wood;
    private String xy;

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

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
        return "City{" + "name=" + name + ", people=" + people + ", food=" + food + ", xy=" + xy + '}';
    }

}
