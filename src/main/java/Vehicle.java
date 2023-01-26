

class Vehicle
{
    String destination; // Stores the end destination of vehicle object
    String entry; // Stores the entrypoint of a vehicle (used for debugging)
    int timeEntered; // Stores the time entered into the town
    int timeParked; // Stores the time entered into the car park

    Vehicle(String Cardestination, int entryTime) // constructor for vehicle
    {
    destination = Cardestination;
    timeEntered = entryTime;
    }


}


