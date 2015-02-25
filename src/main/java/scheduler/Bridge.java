package scheduler;

import java.util.ArrayList;

public class Bridge {
    private final Lane lane1 = new Lane();
    private final Lane lane2 = new Lane();
    private final ArrayList<String> northBound = new ArrayList<String>();
    private final ArrayList<String> southBound = new ArrayList<String>();
    private int lane1Signal, lane2Signal;
    private int weight = 0;

    /**
     * Vehicles arrives on the bridge.
     *
     * @param vehicleId        the vehicle id.
     * @param vehicleType      the {@link VehicleType}.
     * @param vehicleDirection the vehicle {@link Direction}.
     * @throws {@link InterruptedException}.
     */
    public synchronized void arrival(int vehicleId, VehicleType vehicleType, Direction vehicleDirection) throws InterruptedException {
        int count = 1;

        System.out.println(" ");
        System.out.println("****************");
        //We store the Vehicle to the Corresponding
        //Northbound and Southbound waiting Lists depending on their direction

        if (vehicleDirection.getValue() == 1) {
            northBound.add(String.format("%s#%d", vehicleType.toString(), vehicleId));
        } else {
            southBound.add(String.format("%s#%d", vehicleType.toString(), vehicleId));
        }
        System.out.println(String.format("%s#%d [%s] arrived", vehicleType.toString(), vehicleId, vehicleDirection.toString()));

        String vehicle = String.format("%s#%d", vehicleType.toString(), vehicleId);
        while (count == 1) {

            //We check if Bridge lane direction are same with Car direction,no vehicles waiting,
            //   Vehicles on Lane to be less than 3 and weight after addition to be less than 1250
            if (vehicleDirection == lane1.getDirection() && vehicleDirection.getValue() == 0 && lane1Signal != 1 && lane1.getLaneSize() < 3 && weight + vehicleType.getWeight() <= 1250) {
                //when condition satisfied Car added to the bridge lane,
                //removed from Northbound waiting list and weight updated
                lane1.addLanes(vehicle);

                weight = weight + vehicleType.getWeight();
                southBound.remove(vehicle);
                //if Lane 2 was signaled and now Southbound waiting list is now empty
                //then we remove the signal
                if (lane2Signal == 1 && southBound.isEmpty()) {
                    lane2Signal = 0;
                }

                count = 0;
                //We check if Bridge lane direction are same with Van direction,no vehicles waiting,
                //   Vehicles on Lane to be less than 3 and weight after addition to be less than 1250
            } else if (vehicleDirection == lane1.getDirection() && vehicleDirection.getValue() == 1 && lane1.getLaneSize() < 3 && weight + vehicleType.getWeight() <= 1250) {
                //when condition satisfied Van added to the bridge lane,
                //removed from Northbound waiting list and weight updated
                lane1.addLanes(vehicle);
                weight = weight + vehicleType.getWeight();
                northBound.remove(vehicle);
                //if Lane 1 was signaled and now Northbound waiting list is now empty
                //then we remove the signal
                if (lane1Signal == 1 && northBound.isEmpty()) {
                    lane1Signal = 0;
                }
                count = 0;

                //We check if Bridge lane direction are same with Car direction,no vehicles waiting,
                //   Vehicles on Lane to be less than 3 and weight after addition to be less than 1250
            } else if (vehicleDirection == lane2.getDirection() && vehicleDirection.getValue() == 0 && lane2.getLaneSize() < 3 && weight + vehicleType.getWeight() <= 1250) {
                //when condition satisfied Car added to the bridge lane,
                //removed from Southbound waiting list and weight updated
                lane2.addLanes(vehicle);
                weight = weight + vehicleType.getWeight();
                southBound.remove(vehicle);
                //if Lane 2 was signaled and now Southbound waiting list is empty
                //we remove the signal
                if (lane2Signal == 1 && southBound.isEmpty()) {
                    lane2Signal = 0;
                }
                count = 0;
                //We check if Bridge lane direction are same with Car direction,no vehicles waiting,
                //   Vehicles on Lane to be less than 3 and weight after addition to be less than 1250
            } else if (vehicleDirection == lane2.getDirection() && vehicleDirection.getValue() == 1 && lane2Signal != 1 && lane2.getLaneSize() < 3 && weight + vehicleType.getWeight() <= 1250) {
                //when condition satisfied Car added to the bridge lane,
                //removed from Northbound waiting list and weight updated
                lane2.addLanes(vehicle);
                weight = weight + vehicleType.getWeight();

                northBound.remove(vehicle);
                //if Lane 1 was signaled and now Southbound waiting list is empty
                //we remove the signal
                if (lane1Signal == 1 && northBound.isEmpty()) {
                    lane1Signal = 0;
                }
                count = 0;

                //we check if Lane direction and Vehicle direction are different ,
                //the Lane isn't signaled and is empty
            } else if (vehicleDirection.getValue() == 0 && lane1.getDirection().getValue() == 1 && lane1Signal != 1 && lane1.isLaneEmpty() && northBound.isEmpty()) {
                //L1 direction switched from northbound to southbound
                lane2.setDirection(Direction.SOUTH_BOUND);

                count = 1;
                //we check if Lane direction and Vehicle direction are different ,the Lane isn't signaled and is empty
            } else if (vehicleDirection.getValue() == 1 && lane1.getDirection().getValue() == 0 && lane1.isLaneEmpty()) {
                //L1 direction switched from southbound to northbound
                lane2.setDirection(Direction.NORTH_BOUND);
                count = 1;
                //we check if Lane direction and Vehicle direction are different ,the Lane isn't signaled and is empty
            } else if (vehicleDirection.getValue() == 1 && lane2.getDirection().getValue() == 0 && lane2Signal != 1 && lane2.isLaneEmpty() && southBound.isEmpty()) {
                //L1 direction switched from southbound to northbound
                lane2.setDirection(Direction.NORTH_BOUND);
                count = 1;
                //we check if Lane direction and Vehicle direction are different ,the Lane isn't signaled and is empty
            } else if (vehicleDirection.getValue() == 0 && lane2.getDirection().getValue() == 1 && lane2.isLaneEmpty()) {
                //L1 direction switched from northbound to southbound
                lane2.setDirection(Direction.SOUTH_BOUND);
                count = 1;
            } //if Lane L1 direction is southbound and Vehicle direction is northbound and L1 isn't empty
            else if (lane1.getDirection().getValue() == 0 && lane1Signal == 0 && vehicleDirection.getValue() == 1 && lane1.getLaneSize() > 0) {
                //Lane L1 signaled
                lane1Signal = 1;
                count = 1;
                //Corresponding thread of Vehicle goes in the Wait state
                wait();
                count = 1;
                //if Lane L1 direction is northbound and Vehicle direction is southbound and L1 isn't empty
            } else if (lane2.getDirection().getValue() == 1 && vehicleDirection.getValue() == 0 && lane2Signal == 0 && lane2.getLaneSize() > 0) {
                //Lane L2 signaled
                lane2Signal = 1;
                count = 1;
                //Corresponding thread of Vehicle goes in the Wait state
                wait();
                count = 1;
            } else {
                //Corresponding thread of Vehicle goes in the Wait state
                wait();
                count = 1;
            }

        }
        notify();
        //We notify the waiting threads

        System.out.println(" ");
        System.out.println("****************");
        System.out.println("Bridge Status:");
        System.out.println(String.format("Lane 1 - %s %s", lane1.getDirection(), lane1.getLanes().toString()));
        System.out.println(String.format("Lane 2 - %s %s", lane2.getDirection(), lane2.getLanes().toString()));
        System.out.println("Waiting Queue (northBound):" + northBound);

        System.out.println("Waiting Queue (southBound):" + southBound);

    }

    /**
     * Vehicle cross the bridge
     *
     * @param vehicleId   the vehicle id.
     * @param vehicleType the {@link VehicleType}.
     * @throws {@link InterruptedException}.
     */
    public void cross(final int vehicleId, final VehicleType vehicleType) throws InterruptedException {
        System.out.println(" ");
        System.out.println("**************");
        System.out.println(String.format("%s#%d is now crossing the bridge", vehicleType.toString(), vehicleId));

        Thread.sleep(3000);
    }

    /**
     * Vehicle left the bridge
     *
     * @param vehicleId   the vehicle id
     * @param vehicleType the {@link VehicleType}.
     */
    public synchronized void leave(final int vehicleId, final VehicleType vehicleType) {
        System.out.println(" ");
        System.out.println("**************");
        String vehicle = String.format("%s#%d", vehicleType.toString(), vehicleId);
        if (lane1.getLanes().contains(vehicle)) {
            lane1.getLanes().remove(vehicle);
        } else {
            lane2.getLanes().remove(vehicle);
        }
        weight -= vehicleType.getWeight();
        System.out.println(String.format("%s exited the bridge", vehicle));
        System.out.println(String.format("weight on Bridge after Leaving = %d", weight));
        //  threads that are sleeping notified
        System.out.println("Bridge Status:");
        System.out.println(String.format("Lane 1 - %s %s", lane1.getDirection(), lane1.getLanes().toString()));
        System.out.println(String.format("Lane 2 - %s %s", lane2.getDirection(), lane2.getLanes().toString()));
        System.out.println(String.format("Waiting Queue (northBound): %s", northBound.toString()));
        System.out.println(String.format("Waiting Queue (southBound): %s", southBound.toString()));
        notify();

    }

}


