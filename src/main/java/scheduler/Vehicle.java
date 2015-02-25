package scheduler;


/**
 * Object to store information regarding a vehicle.
 *
 * @author Yashraj R. Sontakke
 */
public class Vehicle extends Thread {
    private Bridge bridge;
    private final int vehicleId;
    private final VehicleType vehicleType;
    private final Direction vehicleDirection;

    public Vehicle(final int vehicleId, final VehicleType vehicleType, final Direction vehicleDirection, final Bridge bridge) {
        this.bridge = bridge;
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.vehicleDirection = vehicleDirection;

    }

    public void run() {
        bridge.arrival(vehicleId, vehicleType, vehicleDirection);
        bridge.cross(vehicleId, vehicleType);
        bridge.leave(vehicleId, vehicleType);

    }
}
