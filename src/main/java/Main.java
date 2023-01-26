import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

public class Main {
    public Main() {
    }

    private static void detectDeadlock() //debugging for detecting deadlock
    {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] threadIDs = threadBean.findDeadlockedThreads();
        boolean deadLock = threadIDs != null && threadIDs.length >0;
        System.out.println("Deadlocks found: " + deadLock);
    }

    private static Logger logger = Logger.getLogger("ConcurrencyLog");

    public static void initialiseLogger()
    {
        FileHandler handler;
        try
        {
            handler = new FileHandler("C:/Users/jghaf/Desktop/Conc Logs/conLogs.log");

            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);

            logger.info("Logger initialised");
        }
        catch (Exception e)
        {
            logger.log(Level.WARNING,"Exception ::", e);
        }
    }
    
    public static void main(String[] args) {
        initialiseLogger();

        //Roads New road objects created passing in the capacity of the roads
        Road southToA = new Road(60);
        Road AtoIndustrial = new Road(15);
        Road AtoB = new Road(7);
        Road BtoA = new Road(7);
        Road eastToB = new Road(30);
        Road BtoC = new Road(10);
        Road CtoB = new Road(10);
        Road northToC = new Road(50);
        Road cToShopping = new Road(7);
        Road CtoD = new Road(10);
        Road DtoUni = new Road(15);
        Road DtoStation = new Road(15);

        //EntryPoints string names, mainly used for debugging
        String southEntryName = "South";
        String eastEntryName = "East";
        String northEntryName = "North";

        // Rates for entrypoint production per hour
        int eastCarsPH = 300;
        int northCarsPH = 550;
        int southCarsPH = 550;

        //Car Park Names string names, used for destination confirmation
        String industrialParkName = "Industrial Park";
        String shoppingParkName = "Shopping Centre";
        String stationParkName = "Station";
        String universityParkName = "University";

        // Car Park Spaces, defines the number of spaces each car park has
        int industrialParkSpaces = 1000;
        int shoppingParkSpaces = 400;
        int stationParkSpaces = 150;
        int universityParkSpaces = 100;

        // Junction Threads created passing in junction names, entry roads, exit roads, time spent on green and if it is running (debug)
        Thread junctionA = new Thread(new Junction("A", new Road[] {southToA, BtoA}, new Road[] {AtoB, AtoIndustrial}, 60, true, logger));
        Thread junctionB = new Thread(new Junction("B", new Road[] {eastToB},new Road[] {BtoC, BtoA}, 60, true, logger));
        Thread junctionC = new Thread(new Junction("C", new Road[] {BtoC}, new Road[] {CtoB, cToShopping, CtoD},30, true, logger));
        Thread junctionD = new Thread(new Junction("D", new Road[] {CtoD}, new Road[]{DtoUni, DtoStation}, 30, true, logger));

        // Entrypoint Threads created passing in the road in which they are to produce to, the rate of production and the name of the entrypoint
        Thread southEntry = new Thread(new EntryPoint(southToA, southCarsPH, southEntryName));
        Thread eastEntry = new Thread(new EntryPoint(eastToB, eastCarsPH, eastEntryName));
        Thread northEntry = new Thread(new EntryPoint(northToC, northCarsPH, northEntryName));

        // Car Park Threads created passing in the road to extract from, the name of the car park and the number of spaces available
        Thread IndustrialPark = new Thread(new CarPark(AtoIndustrial,industrialParkName, industrialParkSpaces));
        Thread ShoppingCentre = new Thread(new CarPark(cToShopping, shoppingParkName, shoppingParkSpaces));
        Thread Station = new Thread(new CarPark(DtoStation, stationParkName, stationParkSpaces));
        Thread University = new Thread(new CarPark(DtoUni, universityParkName, universityParkSpaces));

        // Clock New thread created for the clock passing through the simulation time in seconds
        Thread t1 = new Thread(new Clock(3600, universityParkSpaces, stationParkSpaces, shoppingParkSpaces, industrialParkSpaces));


        // Start Threads
        System.out.println("Scenario 1 Config Used");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.start();
        southEntry.start();
        junctionA.start();
        IndustrialPark.start();
        junctionB.start();
        eastEntry.start();
        //junctionC.start();
        //ShoppingCentre.start();
        //northEntry.start();
        //junctionD.start();
        //Station.start();
        //University.start();


        try
        {
            t1.join();
            southEntry.join();
            junctionA.join();
            IndustrialPark.join();
            junctionB.join();
            eastEntry.join();
            junctionC.join();
            ShoppingCentre.join();
            northEntry.join();
            junctionD.join();
            Station.join();
            University.join();

        }
        catch (InterruptedException ignored)
        {
        }


    }
}
