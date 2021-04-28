public class User
{
  private int id;
  private String Username;
  private String Password;

  public User(int id,String username,String password)
  {
    this.id = id;
    Username = username;
    Password = password;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getUsername()
  {
    return Username;
  }

  public void setUsername(String username)
  {
    Username = username;
  }

  public String getPassword()
  {
    return Password;
  }

  public void setPassword(String password)
  {
    Password = password;
  }
}
