import java.io.Serializable;

public class Message implements Serializable
{
  private int command;
  private String json;

  public Message(int command, String json)
  {
    this.command = command;
    this.json = json;
  }

  public int getCommand()
  {
    return command;
  }

  public void setCommand(int command)
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
