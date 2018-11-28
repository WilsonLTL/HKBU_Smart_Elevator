package AppKickstarter.Elevator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ElevatorTime {

    private static float UpOneFloor;
    private static float DownOneFloor;
    private static float AccUp;
    private static float AccDown;
    private static float DecUp;
    private static float DecDown;
    private static float DoorOpen;
    private static float DoorClose;
    private static float DoorWait;

    public ElevatorTime(String cfgPath){
        try {
            Properties cfgProps = new Properties();
            FileInputStream in = new FileInputStream(cfgPath);
            cfgProps.load(in);
            in.close();
            UpOneFloor = Float.parseFloat(cfgProps.getProperty("Elev.Time.UpOneFloor"));
            DownOneFloor =  Float.parseFloat(cfgProps.getProperty("Elev.Time.DownOneFloor"));
            AccUp =  Float.parseFloat(cfgProps.getProperty("Elev.Time.AccUp"));
            AccDown = Float.parseFloat(cfgProps.getProperty("Elev.Time.AccDown"));
            DecUp = Float.parseFloat(cfgProps.getProperty("Elev.Time.DecUp"));
            DecDown = Float.parseFloat(cfgProps.getProperty("Elev.Time.DecDown"));
            DoorOpen = Float.parseFloat(cfgProps.getProperty("Elev.Time.DoorOpen"));
            DoorClose = Float.parseFloat(cfgProps.getProperty("Elev.Time.DoorClose"));
            DoorWait = Float.parseFloat(cfgProps.getProperty("Elev.Time.DoorWait"));
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open config file ("+cfgPath+").");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error reading config file ("+cfgPath+").");
            System.exit(-1);
        }
    }

    public static int getUpOneFloor() {
        return Math.round(1000*UpOneFloor);
    }

    public static int getDownOneFloor() {
        return Math.round(1000* DownOneFloor);
    }

    public static int getAccUp() {
        return Math.round(1000* AccUp);
    }

    public static int getAccDown() {
        return Math.round(1000* AccDown);
    }

    public static int getDecUp() {
        return Math.round(1000* DecUp);
    }

    public static int getDecDown() {
        return Math.round(1000* DecDown);
    }

    public static int getDoorOpen() {
        return Math.round(1000* DoorOpen);
    }

    public static int getDoorClose() {
        return Math.round(1000* DoorClose);
    }

    public static int getDoorWait() {
        return Math.round(1000* DoorWait);
    }
}
