package scheduler;

/**
 * Enum class to represent the direction.
 *
 * @author Yashraj R. Sontakke
 */
public enum Direction {

    NORTH_BOUND(1),
    SOUTH_BOUND(0);

    private Integer value;

    Direction(final Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
