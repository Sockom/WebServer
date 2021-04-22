import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class SocketHandler implements Runnable
{
  private InputStream inputStream;
  private OutputStream outputStream;
  private Gson gson;
  private Socket socket;
  private DBAccess dbAccess;

  public SocketHandler(Socket socket) throws IOException, SQLException
  {
    inputStream = socket.getInputStream();
    outputStream = socket.getOutputStream();
    this.socket = socket;
    gson = new Gson();
    dbAccess = new DBAccess();

  }

  @Override public void run()
  {
    byte[] lenbytes = new byte[1024 * 1024];
    try
    {
      int read = inputStream.read(lenbytes, 0, lenbytes.length);
      String data = new String(lenbytes, 0, read);
      System.out.println(data);
      Message message = gson.fromJson(data, Message.class);

      switch(message.getCommand())
      {
        case "GETCURRENTDATA":
          String[] split = message.getJson().split(":");
          int userid = Integer.parseInt(split[0]);
          int greenhouseid = Integer.parseInt(split[1]);
          double CO2 = dbAccess.DBGetCurrentData(userid, greenhouseid);
          byte[] bytes = sendStatus("SUCCESS", ""+CO2);
          outputStream.write(bytes, 0, bytes.length);
          break;

      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  private byte[] sendStatus(String e, String string)
  {
    Message message = new Message();
    message.setCommand(e);
    message.setJson(string);
    String serialize = gson.toJson(message);
    return serialize.getBytes();
  }
}
