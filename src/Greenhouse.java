import java.util.ArrayList;

public class Greenhouse
{
  private ArrayList Plants;
  private int Id;
  private String Name;
  private float Temperature;
  private float CO2;
  private float Humidity;
  private int UserID;

  public Greenhouse(int id, String name, float temperature,
      float CO2, float humidity, int userID)
  {
    Plants = new ArrayList();
    Id = id;
    Name = name;
    Temperature = temperature;
    this.CO2 = CO2;
    Humidity = humidity;
    UserID = userID;
  }

  public void AddPlant(Plant plant)
  {
     Plants.add(plant);
  }

  public void RemovePlant(Plant plant)
  {
    Plants.remove(plant);
  }

  public ArrayList getPlants()
  {
    return Plants;
  }

  public void setPlants(ArrayList plants)
  {
    Plants = plants;
  }

  public int getId()
  {
    return Id;
  }

  public void setId(int id)
  {
    Id = id;
  }

  public String getName()
  {
    return Name;
  }

  public void setName(String name)
  {
    Name = name;
  }

  public float getTemperature()
  {
    return Temperature;
  }

  public void setTemperature(float temperature)
  {
    Temperature = temperature;
  }

  public float getCO2()
  {
    return CO2;
  }

  public void setCO2(float CO2)
  {
    this.CO2 = CO2;
  }

  public float getHumidity()
  {
    return Humidity;
  }

  public void setHumidity(float humidity)
  {
    Humidity = humidity;
  }

  public int getUserID() {
    return UserID;
  }
}
