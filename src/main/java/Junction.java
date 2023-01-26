import java.util.Objects;
import java.util.Timer; // used for light switching
import java.util.TimerTask;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;


public class Junction implements Runnable
{
    Clock lightTimer; // clock object
    Timer timer; // Timer object
    String junctionName; // String val to hold junction name

    Road [] entries; // array of entry roads
    Road [] exits; // array of exit roads

    private int timeOnGreen; // int value to hold time on green
    private int timeOnGreenOrigin; // int value to hold time on green which is used to reset the green light value which ticks down
    private int greenTracker = 0; // used to switch the junction entry road
    private int carsThrough = 0; // stores the number of cars sent through
    private String fromThis; //  stores the name of where the cars came from

    Logger logger; // logging


    boolean running = false;

    public Junction (String name, Road[]theEntries, Road[]theExits, int greenTimer, boolean runner, Logger log) // junction constructor
    {
        junctionName = name; // sets the name of the junction

        entries = theEntries; // stores all possible entry roads
        exits = theExits; // stores all possible exit roads

        timeOnGreen = greenTimer; // sets time on green
        timeOnGreenOrigin = greenTimer; // same as above
        running = runner; // sets the junction to run
        timer = new Timer();

        logger = log;
    }

    public void lightControl ()
    {
        if (Clock.running == true)
        {
            timer.schedule(new TimerTask() {
                @Override
                public void run() { // this timer is used to increment the greenTracker every 30 sim seconds, also this is where the junction log their activities
                    timeOnGreen--; // negates 1 every tick to simulate countdown
                    if (timeOnGreen == 0) // checks when countdown is done
                    {
                        timeOnGreen = timeOnGreenOrigin; // resets counter with original value
                        if(greenTracker == (entries.length-1)) // ensures that the light is switched based on the number of junctions available
                        {
                            logger.info("Junction Switched" + "Time: " + getTime() + " Junction" + junctionName + ": " + carsThrough + " cars through from " + fromThis + ", " + entries[greenTracker].carsOnRoad() + "cars waiting.");
                            greenTracker = 0; // when equal the tracker is reset so it goes back to the first junction
                            carsThrough = 0;
                            //System.out.println("**********Light Reset" + junctionName); // for debugging
                        }
                        else
                        {
                            logger.info("Junction Switched" + "Time: " + getTime() + " Junction" + junctionName + ": " + carsThrough + " cars through from " + fromThis + ", " + entries[greenTracker].carsOnRoad() + "cars waiting.");
                            carsThrough = 0;
                            greenTracker++; // switches to next junction
                            //System.out.println("Light Change**********"+ junctionName); // for debugging
                        }
                    }
                }
            }, 0, 100);
        }
    }


    public void handler() {
        Vehicle crossing = null;
        Road destination = null;
        // these statements check the value outputted from the traffic light timer and switch the input road accordingly
        if(greenTracker == 0)
        {
            // decides where the cars came from dependent on junction
            if (junctionName.equals("A"))
            {
                fromThis = "South";
            }
            if (junctionName.equals("B"))
            {
                fromThis = "East";
            }
            if (junctionName.equals("C"))
            {
                fromThis = "North";
            }
            if (junctionName.equals("D"))
            {
                fromThis = "East";
            }
            crossing = entries[0].extract(); // car taken from specified entry route
            carsThrough++; // increments the number of cars sent through
        }
        if (greenTracker == 1)
        {
            if (junctionName.equals("A"))
            {
                fromThis = "North";
            }
            if (junctionName.equals("B"))
            {
                fromThis = "South";
            }
            if (junctionName.equals("C"))
            {
                fromThis = "South";
            }
            crossing = entries[1].extract();
            carsThrough++;
        }
        if (greenTracker == 2)
        {
            if (junctionName.equals("B"))
            {
                fromThis = "North";
            }
            crossing = entries[2].extract();
            carsThrough++;
        }

        // the next of block of code is used to locate cars to their correct exits so that they get to the correct destination
        // the location stored on each vehicle object is read and is sent to an exit which matches

        if (crossing != null) // ensures that value is not null
        {
            if (junctionName == "A")
            {
                //System.out.println("Junction A"); // debugging to make sure that junction is functioning
                if(crossing.destination == "Industrial Park")
                {
                    destination = exits[1];
                }
                else
                {
                    destination = exits[0];
                }
            }

            if (junctionName == "B")
            {
                //System.out.println("Junction B");
                if(crossing.destination == "Industrial Park")
                {
                    destination = exits[1];
                }
                else
                {
                    destination = exits[0];
                }
            }

            if (junctionName == "C")
            {
                if(crossing.destination == "Industrial Park")
                {
                    destination = exits[0];
                }
                else if (crossing.destination == "Shopping Centre")
                {
                    destination = exits[1];
                }
                else
                {
                    destination = exits[2];
                }
            }

            if (junctionName == "D")
            {
                if(crossing.destination == "University")
                {
                    destination = exits[0];
                }
                else
                {
                    destination = exits[1];
                }
            }
            destination.insert(crossing); // inserts vehicle at determined exit road
            //System.out.println("Car sent through"); // debugging use

            try {
                sleep(500); // sleeps to simulate car moving across junction
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    public String getTime()
    {
        int time = Clock.clockVal;
        String returnedTime = String.format("%02d:%02d", time / 60 , time % 60);
        return returnedTime;
    }

    public void run()
    {
        lightControl(); // runs the light control
        while(running)
        {
            handler(); // runs the exit manager
        }
    }



}
