public class DataContainer
{
  private double data;
  private DataType type;

  public DataContainer(double data, DataType type)
  {
    this.data = data;
    this.type = type;
  }

  public double getData()
  {
    return data;
  }

  public void setData(double data)
  {
    this.data = data;
  }

  public DataType getType()
  {
    return type;
  }

  public void setType(DataType type)
  {
    this.type = type;
  }
}
