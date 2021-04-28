import com.google.gson.Gson;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;


public class SensorData {

    byte[] Data;
    Timestamp timestamp;
    Gson gson;

    public SensorData() {
        gson = new Gson();
        Random tal = new Random();
        int[] temperatur = new int[15];
        int[] CO2 = new int[15];
        int[] luftfugtighed = new int[15];
        Timestamp[] timestamps = new Timestamp[15];
        for (int i = 0 ; i < 15 ; i++) {
            CO2[i] = tal.nextInt(1600);
            temperatur[i] = tal.nextInt(100);
            luftfugtighed[i] = tal.nextInt(100);
            timestamps[i] = new Timestamp(new Date().getTime()-60000 * i);
        }
        ByteArray byteArray = new ByteArray(1, CO2, timestamps, temperatur, luftfugtighed, 15);

        System.out.println(gson.toJson(byteArray));
    }


}
