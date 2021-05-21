
import java.io.Serializable;

public class Message implements Serializable
{
  private String command;
  private String json;


  public String getCommand()
  {
    return command;
  }

  public void setCommand(String command)
  {
    this.command = command;
  }

  public String getJson()
  {
    return json;
  }

  public void setJson(String json)
  {
    this.json = json;
  }
}
