

import java.sql.Timestamp;

public class ByteArray {

    private int drivhusID;
    private int numberOf;
    private Timestamp[] Time;
    private int[] CO2;
    private int[] temperaturMåling;
    private int[] luftfugtighed;


    public ByteArray(int drivhusID, int[] CO2, Timestamp[] Time, int[] temperaturMåling, int[] luftfugtighed, int numberOf) {
        this.drivhusID = drivhusID;
        this.Time = Time;
        this.CO2 = CO2;
        this.temperaturMåling = temperaturMåling;
        this.luftfugtighed = luftfugtighed;
        this.numberOf = numberOf;
    }
}
