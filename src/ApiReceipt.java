import java.sql.Timestamp;

public class ApiReceipt
{
  private Timestamp timeOfExecution;

  public ApiReceipt(Timestamp timeOfExecution)
  {
    this.timeOfExecution = timeOfExecution;
  }

  public Timestamp getTimeOfExecution()
  {
    return timeOfExecution;
  }

  public void setTimeOfExecution(Timestamp timeOfExecution)
  {
    this.timeOfExecution = timeOfExecution;
  }
}
