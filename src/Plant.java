public class Plant
{
  private int Id;
  private String Name;
  private int PlantScore;
  private float HumidityRequirement;
  private float TemperatureRequirement;
  private float CO2Requirement;
  private int DrivhusID;

  public Plant(int id, String name, int plantScore, float humidityRequirement,
      float temperatureRequirement, float CO2Requirement, int drivhusID)
  {
    Id = id;
    Name = name;
    PlantScore = plantScore;
    HumidityRequirement = humidityRequirement;
    TemperatureRequirement = temperatureRequirement;
    this.CO2Requirement = CO2Requirement;
    DrivhusID = drivhusID;
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

  public int getDrivhusID() {
    return DrivhusID;
  }
}
