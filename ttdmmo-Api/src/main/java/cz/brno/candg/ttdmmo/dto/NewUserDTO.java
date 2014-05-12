package cz.brno.candg.ttdmmo.dto;

/**
 * DTO for new user
 *
 * @author lastuvka
 */
public class NewUserDTO {

    private String name;
    private String email;
    private String user_id;
    private String city;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int findUser_num() {
        String[] splits = user_id.split(":");
        return Integer.parseInt(splits[1]);
    }

    @Override
    public String toString() {
        return "NewUserDTO{" + "name=" + name + ", email=" + email + ", user_id=" + user_id + ", city=" + city + '}';
    }

}
