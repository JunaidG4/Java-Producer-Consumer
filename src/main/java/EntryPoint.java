import java.util.Random;

class EntryPoint implements Runnable {
    String entryP; // stores entry point name
    String finalDestination; // for debug
    Road buf; // stores the road which cars should enter onto
    int number; // stores the number of cars to make
    int productionRate; // stores how many cars per hour should be made
    int carsMade; // stores how many cars were actually made


    public String rndDest() // method to assign new vehicle objects a weighted location
    {
        Random random = new Random();
        int rand_num = random.nextInt(100); // generates a random number between 1 and 100

        if(rand_num <=10) // 10% chance
        {
            return "University";
        }
        else if (rand_num <=30) // 20% chance
        {
            return "Station";
        }
        else if (rand_num <=60) // 30% chance
        {
            return "Shopping Centre";
        }
        else // final 40%
        {
            return "Industrial Park";
        }

    }

    public String rndDestDebug() // method to assign new vehicle objects a weighted location
    {
        Random randomDebug = new Random();
        int rand_num = randomDebug.nextInt(100); // generates a random number between 1 and 100

        if(rand_num <=40) // 10% chance
        {
            return "Shopping Centre";
        }
        else // final 40%
        {
            return "Industrial Park";
        }

    }


    public EntryPoint(Road b, int numItems, String entryName) // constructor for entrypoint
    {
        buf = b;
        number = numItems;
        entryP = entryName;
        productionRate = ((3600 / number) * 100); // production rate calculated in simulation times
    }
    public void produce(int i)
    {
        while (Clock.running == true) // boolean flag for running
        {
            Vehicle car = new Vehicle("Industrial Park", Clock.clockVal); // creates a new vehicle object and assigns the time it entered
            //System.out.println("Entrypoint "+entryP+" sending car: "+car.destination+i);
            try {
                Thread.sleep(productionRate); // sleeps according to production rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buf.insert(car); // car enters road network
            carsMade++; // adds to cars made
            createdUpdate(carsMade); // method for storing the number of cars made accoring to which entry point is running
        }

    }

    public void createdUpdate (int carsCreated) // method sends each entry point production value to the clock class for final reports
    {
        if (entryP == "South")
        {
            Clock.southProduced = carsCreated;
        }
        if (entryP == "East")
        {
            Clock.eastProduced = carsCreated;
        }
        if (entryP == "North")
        {
            Clock.northProduced = carsCreated;
        }
    }

    public void run() {

        for (int i=0; i < number; i++){ // runs for the number of cars that are entered

             produce(i);

        }
    }
}