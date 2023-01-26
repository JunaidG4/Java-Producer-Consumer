

import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class Road {


    public Vehicle [] roadArray; // creates an array of vehicle objects to allow cars onto the road
    public int nextIn=0; // pointer for array
    public int nextOut=0;

    // semaphores used to avoid deadlock and to ensure mutual exclusion
    private Semaphore mutex = new Semaphore(1); // how many threads can access
    private Semaphore items; // number of cars
    private Semaphore spaces; // number of spaces on road

    int arraySize; // defines the size of the array


    public Road(int size){ // road constructor
        roadArray = new Vehicle[size]; // creates new road array of given size
        spaces = new Semaphore(size); // same size used for this semaphore to allow number of object permits
        items = new Semaphore(0);
        arraySize = size;
    }

    // this is the method used for inserting cars onto the road
    // threads must acquire a spaces semaphore which lets them know if there is space to add onto the road
    // then they must acquire the mutex to ensure mutual exclusion
    // if the next in int is not equal to the array size then a vehicle is added to array and pointer incremented
    // both sempahores are then released
    public void insert(Vehicle item) {

        try {
            spaces.acquire();
            mutex.acquire();
            if (nextIn != arraySize) {
                roadArray[nextIn] = item;
                nextIn++;
            }
            items.release();
            mutex.release();
        } catch (Exception e) {
            System.out.println("ERROR WHEN INSERTING: "+e.getMessage());
        }

    }

    // this is the extract methos which

    public Vehicle extract(){
        try {
            items.acquire();
            mutex.acquire();
            if (nextIn != 0) {
                Vehicle result = roadArray[0];;
                Vehicle[] copyDown = new Vehicle[roadArray.length];
                for (int i = 1; i < roadArray.length; i++) {
                    copyDown[i - 1] = roadArray[i];
                }
                roadArray = copyDown;
                nextIn--;
                spaces.release();
                mutex.release();
                return result;
            } else {
                spaces.release();
                mutex.release();
                return null;
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR WHEN EXTRACTING: "+ex.getMessage());
            return null;
        }
    }

    public int carsOnRoad() // a method to return the number of cars on the road as the pointer location shows how many cars are in the array
    {
        int onRoad = (nextIn);
        return onRoad; // returns the value
    }
}
