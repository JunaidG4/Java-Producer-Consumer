public class Clock implements Runnable
{
    public volatile static int clockVal; // integer to store value of the clock
    public int simulationTime; // stores the passed through time for simulation
    String time = String.format("%02d:%02d", clockVal / 60 , clockVal % 60); // Formats the clock value into time format MM:SS

    public volatile static int universitySpaces;
    public volatile static int stationSpaces;
    public volatile static int shoppingSpaces;
    public volatile static int industrialSpaces;

    public volatile static int uniParked;
    public volatile static int stationParked;
    public volatile static int shoppingParked;
    public volatile static int industrialParked;

    public volatile static int uniAv;
    public volatile static int stationAv;
    public volatile static int shoppingAv;
    public volatile static int industrialAv;

    public volatile static int northProduced;
    public volatile static int eastProduced;
    public volatile static int southProduced;

    public volatile static boolean running = true; // volatile ensures that local caching doesnt happen in other classes (thread safety)

    int boardPulse = 600;

    Clock(int simTime, int uniSpaces, int statSpaces, int shopSpaces, int industSpaces)
    {
        universitySpaces = uniSpaces;
        stationSpaces = statSpaces;
        shoppingSpaces = shopSpaces;
        industrialSpaces = industSpaces;

        simulationTime = simTime;

    } // constructor for clock object

    public void clockTimer() throws InterruptedException
    {
        while(clockVal < simulationTime) // ensures the clock runs for the entered simulation time
        {
            clockVal = (clockVal + 1); // ticks clock
            time = String.format("%02d:%02d", clockVal / 60 , clockVal % 60); // Formats the clock value into time format MM:SS
            //System.out.println(time); // prints time (debugging)
            Thread.sleep(100); // ensures clock ticks 10 times per second

            if(clockVal == boardPulse) // every 600 ticks this code runs and displays the board with number of spaces left in each car park
            {
                int outputTime = boardPulse;
                boardPulse = (boardPulse + 600);
                System.out.println("-----------------------------------");
                System.out.println("Time: " + String.format("%02d", outputTime / 60) + "m");
                System.out.println("Industrial Park: " + industrialSpaces);
                System.out.println("Shopping Centre: " + shoppingSpaces);
                System.out.println("Station: " + stationSpaces);
                System.out.println("University: " + universitySpaces);
                System.out.println("-----------------------------------");
            }
        }
        if(clockVal == simulationTime)
        {   // prints final figures, this is a debug example as the program was failing to work in some configs
            int totalCreated = (northProduced + eastProduced + southProduced);
            int totalParked = (uniParked + stationParked + shoppingParked + industrialParked);
            running = false;
            System.out.println("Industrial: " + industrialParked + " Cars parked" + ", average journey time " +  String.format("%02dm:%02ds", industrialAv / 60 , industrialAv % 60));
            System.out.println("Total Cars Entered: " + totalCreated + " | Total Cars Parked: " + totalParked + " | Cars waiting: " + (totalCreated - totalParked));
            System.exit(0); // system exits when time is up and everything is shown

        }
    }

    public void run()
    {
        try {
            clockTimer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
