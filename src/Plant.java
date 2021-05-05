public class Plant
{
  private int plantID;
  private int greenHouseID;
  private String Name;
  private int PlantScore;
  private float HumidityRequirement;
  private float TemperatureRequirement;
  private float CO2Requirement;

  public Plant(int id,int greenHouseID, String name, int plantScore, float humidityRequirement,
      float temperatureRequirement, float CO2Requirement)
  {
    plantID = id;
    this.greenHouseID = greenHouseID;
    Name = name;
    PlantScore = plantScore;
    HumidityRequirement = humidityRequirement;
    TemperatureRequirement = temperatureRequirement;
    this.CO2Requirement = CO2Requirement;
  }

  public int getPlantID()
  {
    return plantID;
  }

  public void setPlantID(int plantID)
  {
    this.plantID = plantID;
  }

  public int getGreenHouseID()
  {
    return greenHouseID;
  }

  public void setGreenHouseID(int greenHouseID)
  {
    this.greenHouseID = greenHouseID;
  }

  public String getName()
  {
    return Name;
  }

  public void setName(String name)
  {
    Name = name;
  }

  public int getPlantScore()
  {
    return PlantScore;
  }

  public void setPlantScore(int plantScore)
  {
    PlantScore = plantScore;
  }

  public float getHumidityRequirement()
  {
    return HumidityRequirement;
  }

  public void setHumidityRequirement(float humidityRequirement)
  {
    HumidityRequirement = humidityRequirement;
  }

  public float getTemperatureRequirement()
  {
    return TemperatureRequirement;
  }

  public void setTemperatureRequirement(float temperatureRequirement)
  {
    TemperatureRequirement = temperatureRequirement;
  }

  public float getCO2Requirement()
  {
    return CO2Requirement;
  }

  public void setCO2Requirement(float CO2Requirement)
  {
    this.CO2Requirement = CO2Requirement;
  }
}
