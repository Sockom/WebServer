import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketHandler implements Runnable
{
  private InputStream inputStream;
  private OutputStream outputStream;
  private Gson gson;
  private Socket socket;

  public SocketHandler(Socket socket) throws IOException
  {
    inputStream = socket.getInputStream();
    outputStream = socket.getOutputStream();
    this.socket = socket;
    gson = new Gson();

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
        case 1:
          byte[] bytes = sendStatus(1, "TILLYKKE");
          outputStream.write(bytes, 0, bytes.length);
          break;

        /*case 1:
          byte[] bytes = sendStatus(Enum.SUCCESS, "TILLYKKE");
          outputStream.write(bytes, 0, bytes.length);
          break;*/
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  private byte[] sendStatus(int e, String string)
  {
    Message message = new Message(e, string);
    String serialize = gson.toJson(message);
    return serialize.getBytes();
  }
}
