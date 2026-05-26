package app.entities;

public class Zip {
    int id;
    String zipcode;
    String town;

    public Zip(int id, String zipcode, String town) {
        this.id = id;
        this.zipcode = zipcode;
        this.town = town;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getTown() {
        return town;
    }
}
