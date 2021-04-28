import java.sql.Timestamp;
import java.util.List;

public class ApiCurrentDataPackage
{
  private List<DataContainer> data;
  private Timestamp lastDataPoint;

  public ApiCurrentDataPackage(List<DataContainer> data,
      Timestamp lastDataPoint)
  {
    this.data = data;
    this.lastDataPoint = lastDataPoint;
  }

  public List<DataContainer> getData()
  {
    return data;
  }

  public void setData(List<DataContainer> data)
  {
    this.data = data;
  }

  public Timestamp getLastDataPoint()
  {
    return lastDataPoint;
  }

  public void setLastDataPoint(Timestamp lastDataPoint)
  {
    this.lastDataPoint = lastDataPoint;
  }
}
