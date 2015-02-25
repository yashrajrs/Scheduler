package scheduler;

/**
 * Enum to store vehicle type.
 *
 * @author Yashraj R. Sontakke
 */
public enum VehicleType {
    VAN(0, 300),
    CAR(1, 200);

    private Integer value;
    private Integer weight;

    VehicleType(final Integer value,final Integer weight) {
        this.value = value;
        this.weight = weight;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getWeight(){
        return weight;
    }
}
