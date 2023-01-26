/*
 * Consumer.java
 *
 * Created on 21 January 2008, 22:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

public class CarPark implements Runnable{
    String cpName; // string to store the name of the car park
    Road entryRoad; // new road object which is where vehicle objects will come from
    private int spacesLeft; // int value to store the number of spaces left
    private int parkedCars; // int value to store the number of cars parked
    private int capacity; // separate int value for capacity
    int overallTime = 0; // running total of travel time
    int averageTime = 0; // running average travel time
    boolean running = true; // running bool
    Vehicle[] vehicles; // an array of vehicle objects where cars will "park"


    public CarPark(Road b, String carParkName, int noOfSpaces) { // car park constructor
        entryRoad = b; // sets the entry road object to entry road
        cpName = carParkName; // sets the car park name
        spacesLeft = noOfSpaces; // sets number of spaces
        capacity = noOfSpaces;
        vehicles = new Vehicle[capacity]; // new array of vehicle objects with size of spaces passed through
    }

    public boolean consume() throws InterruptedException {
        //String nextItem = buf.extract();

        while (Clock.running == true)
        {
            Vehicle nextEntrant = entryRoad.extract(); // extracts a vehicle from the road
            if (spacesLeft > 0) { // only runs as long as the car park has spaces

                try {
                    nextEntrant.timeParked = Clock.clockVal; // sets the time of parking
                    String parked = String.format("%02d:%02d", nextEntrant.timeParked / 60 , nextEntrant.timeParked % 60);
                    vehicles[parkedCars] = nextEntrant; // adds the vehicle to the array ("parks")
                    parkedCars++; // increments number of cars parked
                    spacesLeft--; // decrements number of spaces left
                    clockUpdate(spacesLeft); // updates the number of spaces left
                    //System.out.println("Carpark " + cpName + " admitting " + nextEntrant + " " + nextEntrant.destination + " " + parked); // debugging to show cars are actually being parked
                    //System.out.println("Time to park: " + timeToPark(nextEntrant.timeEntered, nextEntrant.timeParked));
                    timeToPark(nextEntrant.timeEntered, nextEntrant.timeParked);
                    //System.out.println("Spaces left: " + spacesLeft + " "); // debugging to show number of spaces left
                    Thread.sleep(1200); // simulates cars going through barrier
                } catch (NullPointerException e) {
                    System.out.println("Parked Cars: " + parkedCars);
                    System.exit(1);
                }

                return !nextEntrant.equals("****");
            }
            else
            {
                System.out.println("Parked Cars: " + parkedCars);

            }
            return false;
        }

        return false;
    }

    public String timeToPark(int entry, int park) // calculates how long each vehicle took to park
    {
        int travelTime = (park - entry);

        overallTime = overallTime + travelTime;

        averageTime = overallTime / parkedCars;
        String returnedTime = String.format("%02d:%02d", travelTime / 60 , travelTime % 60);

        return returnedTime;
    }

    public void clockUpdate(int spacesLeft) // method to update the noticeboard based on which carpark the thread is
    {
        if(cpName == "Industrial Park")
        {
            Clock.industrialSpaces = spacesLeft;
            Clock.industrialParked = parkedCars;
            Clock.industrialAv = averageTime;
        }
        if(cpName == "Shopping Park")
        {
            Clock.shoppingSpaces = spacesLeft;
            Clock.shoppingParked = parkedCars;
        }
        if(cpName == "University")
        {
            Clock.universitySpaces = spacesLeft;
            Clock.uniParked = parkedCars;
        }
        if(cpName == "Station")
        {
            Clock.stationSpaces = spacesLeft;
            Clock.stationParked = parkedCars;
        }
    }

    public void run()
    {
        try {
            while(running == true)
            {
                consume();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
