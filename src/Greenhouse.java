import java.sql.Timestamp;
import java.util.ArrayList;

public class Greenhouse
{
  private ArrayList<Plant> Plants;
  private int greenHouseID;
  private int userID;
  private String Name;
  private int waterFrequency;
  private double waterVolume;
  private String waterTimeOfDay;
  private Timestamp lastWaterDay;
  private Timestamp lastMeasurement;
  private ArrayList<SensorDataFromAndroid> sensorData;


  public Greenhouse(String name, int greenHouseID,int userID,ArrayList<Plant> plants,
      int waterFrequency,double waterVolume,String waterTimeOfDay,Timestamp lastWaterDay,ArrayList<SensorDataFromAndroid> sensorData)
  {
    Plants = new ArrayList();
    this.greenHouseID = greenHouseID;
    Name = name;
    this.userID = userID;
    this.Plants = plants;
    this.waterFrequency = waterFrequency;
    this.waterVolume = waterVolume;
    this.waterTimeOfDay = waterTimeOfDay;
    this.lastWaterDay = lastWaterDay;
    this.sensorData = sensorData;
  }

  public void AddPlant(Plant plant)
  {
     Plants.add(plant);
  }

  public void RemovePlant(Plant plant)
  {
    Plants.remove(plant);
  }

  public ArrayList<Plant> getPlants()
  {
    return Plants;
  }

  public void setPlants(ArrayList<Plant> plants)
  {
    Plants = plants;
  }

  public int getGreenHouseID()
  {
    return greenHouseID;
  }

  public void setGreenHouseID(int greenHouseID)
  {
    this.greenHouseID = greenHouseID;
  }

  public int getUserID()
  {
    return userID;
  }

  public void setUserID(int userID)
  {
    this.userID = userID;
  }

  public String getName()
  {
    return Name;
  }

  public void setName(String name)
  {
    Name = name;
  }

  public int getWaterFrequency()
  {
    return waterFrequency;
  }

  public void setWaterFrequency(int waterFrequency)
  {
    this.waterFrequency = waterFrequency;
  }

  public double getWaterVolume()
  {
    return waterVolume;
  }

  public void setWaterVolume(double waterVolume)
  {
    this.waterVolume = waterVolume;
  }

  public String getWaterTimeOfDay()
  {
    return waterTimeOfDay;
  }

  public void setWaterTimeOfDay(String waterTimeOfDay)
  {
    this.waterTimeOfDay = waterTimeOfDay;
  }

  public Timestamp getLastWaterDay()
  {
    return lastWaterDay;
  }

  public void setLastWaterDay(Timestamp lastWaterDay)
  {
    this.lastWaterDay = lastWaterDay;
  }

  public Timestamp getLastMeasurement()
  {
    return lastMeasurement;
  }

  public void setLastMeasurement(Timestamp lastMeasurement)
  {
    this.lastMeasurement = lastMeasurement;
  }

  public ArrayList<SensorDataFromAndroid> getSensorData()
  {
    return sensorData;
  }

  public void setSensorData(ArrayList<SensorDataFromAndroid> sensorData)
  {
    this.sensorData = sensorData;
  }
}
