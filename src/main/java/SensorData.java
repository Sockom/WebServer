


import com.google.gson.Gson;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;


public class SensorData {

    private byte[] Data;
    private Gson gson;
    private int[] temperatur = new int[15];
    private int[] CO2 = new int[15];
    private int[] luftfugtighed = new int[15];
    private Timestamp[] timestamps = new Timestamp[15];
    private int drivhusID;
    private int numberOf;


    public SensorData() {
        gson = new Gson();
        Random tal = new Random();
        drivhusID = 1;
        numberOf = 15;
        for (int i = 0 ; i < 15 ; i++) {
            CO2[i] = tal.nextInt(1600);
            temperatur[i] = tal.nextInt(100);
            luftfugtighed[i] = tal.nextInt(100);
            timestamps[i] = new Timestamp(new Date().getTime()-60000 * i);
        }
        ByteArray byteArray = new ByteArray(drivhusID, CO2, timestamps, temperatur, luftfugtighed, 15);

        System.out.println(gson.toJson(byteArray));
    }

    public int[] getTemperatur() {
        return temperatur;
    }

    public int[] getCO2() {
        return CO2;
    }

    public int[] getLuftfugtighed() {
        return luftfugtighed;
    }

    public Timestamp[] getTimestamps() {
        return timestamps;
    }

    public int getDrivhusID() {
        return drivhusID;
    }

    public int getNumberOf() {
        return numberOf;
    }
}
