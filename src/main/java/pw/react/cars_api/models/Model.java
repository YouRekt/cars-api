package pw.react.cars_api.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(unique = true)
    private String name;

    @Column
    private Long productionYear;

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;

    @Column
    private Long fuelCapacity;

    @Column
    private Long seatCount;

    @Column
    private Long doorCount;

    @Column
    private BigDecimal dailyRate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Long productionYear) {
        this.productionYear = productionYear;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Long getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(Long fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public Long getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Long seatCount) {
        this.seatCount = seatCount;
    }

    public Long getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(Long doorCount) {
        this.doorCount = doorCount;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }
}
