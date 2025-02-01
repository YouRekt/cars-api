package pw.react.cars_api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String postalCode;

    @Column
    private String street;

    @Column
    private Integer houseNumber;

    @Formula("concat(street, ' ', house_number, ', ', postal_code, ' ', city, ', ', country)")
    @Column(insertable = false, updatable = false)
    private String fullAddress;

    @Column
    private Point coordinates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }
}
