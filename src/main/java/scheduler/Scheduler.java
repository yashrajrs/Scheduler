package scheduler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Scheduler to perform scheduling of vehicles on bridges.
 *
 * @author Yashraj R. Sontakke
 */
public class Scheduler {

    static int totalVehicles = 0;
    private int groups;
    private ArrayList<Double> northBoundProbabilities, southBoundProbabilities;
    private ArrayList<Integer> vehicles, delay;
    public static Vehicle[] vehicleList;

    public void input() {
        System.out.println("Enter the no. of groups");
        Scanner input = new Scanner(System.in);
        groups = input.nextInt();
        System.out.println(String.format("No of Groups = %d", groups));
        northBoundProbabilities = new ArrayList<Double>(groups);
        southBoundProbabilities = new ArrayList<Double>(groups);
        vehicles = new ArrayList<Integer>(groups);
        delay = new ArrayList<Integer>(groups - 1);

        for (int i = 0; i < groups; i++) {
            System.out.println(String.format("Enter the no.of Vehicles for Group %d", i + 1));
            vehicles.add(i, input.nextInt());
            totalVehicles += vehicles.get(i);
            System.out.println(String.format("Enter the probability for Northbound for Group %d", i + 1));
            northBoundProbabilities.add(input.nextDouble());
            System.out.println(String.format("Enter the probability for Southbound for Group %d", i + 1));
            southBoundProbabilities.add(input.nextDouble());
            if (i != groups - 1) {
                System.out.println(String.format("Enter the delay for Group %d", i + 2));
                delay.add(input.nextInt());
            }
        }
    }

    /**
     * Schedules the vehicles on the bridges.
     */
    public void schedule() {
        final Random random = new Random();
        double randomNumber1, randomNumber2;
        vehicleList = new Vehicle[totalVehicles + 1];
        Bridge bridges = new Bridge();
        int vehicleId = 0;
        VehicleType vehicleType;
        Direction vehicleDirection;
        for (int i = 0; i < groups; i++) {
            for (int j = 0; j < vehicles.get(i); j++) {
                //Use random number to calculate the vehicle type and direction
                randomNumber1 = random.nextDouble();
                randomNumber2 = random.nextDouble();

                vehicleId++;
                if (randomNumber1 < northBoundProbabilities.get(i)) {
                    vehicleDirection = Direction.NORTH_BOUND;
                    if (randomNumber2 < 0.5) {
                        vehicleType = VehicleType.VAN;
                    } else {
                        vehicleType = VehicleType.CAR;
                    }
                } else {
                    vehicleDirection = Direction.SOUTH_BOUND;
                    if (randomNumber2 < 0.5) {
                        vehicleType = VehicleType.VAN;
                    } else {
                        vehicleType = VehicleType.CAR;
                    }
                }
                vehicleList[vehicleId] = new Vehicle(vehicleId, vehicleType, vehicleDirection, bridges);
                System.out.println("Vehicle added " + vehicleId);
                vehicleList[vehicleId].start();
            }
            if (groups != 1 && i != groups - 1) {

                try {
                    Thread.sleep(delay.get(i) * 1000);
                } catch (InterruptedException e) {
                }
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Scheduler scheduler = new Scheduler();
        scheduler.input();
        scheduler.schedule();

    }
}
