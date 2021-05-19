import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public class SocketHandler implements Runnable
{
  private InputStream inputStream;
  private OutputStream outputStream;
  private Gson gson;
  private Socket socket;
  private DBAccess dbAccess;
  private WebSocketClient wSC;

  public SocketHandler(Socket socket, WebSocketClient webSocketClient) throws IOException, SQLException
  {
    inputStream = socket.getInputStream();
    outputStream = socket.getOutputStream();
    this.socket = socket;
    gson = new Gson();
    wSC=webSocketClient;
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
      //startGetDataThread("wss://iotnet.cibicom.dk/app?token=vnoTvwAAABFpb3RuZXQuY2liaWNvbS5kazR2J0Aa7A9xrXbjCeaG1bU=");
      Message message = gson.fromJson(data, Message.class);

      switch(message.getCommand())
      {
        case "GETCURRENTDATA":
        {
          String[] split = message.getJson().split(":");
          int userid = Integer.parseInt(split[0]);
          int greenhouseid = Integer.parseInt(split[1]);
          ApiCurrentDataPackage aPackage = dbAccess.DBGetCurrentData(userid, greenhouseid);
          String stringSerialized = gson.toJson(aPackage);
          byte[] bytes = sendStatus("SUCCESS", "" + stringSerialized);
          outputStream.write(bytes, 0, bytes.length);
          break;
        }

        case "GETUSER":
        {
          String[] split = message.getJson().split(":");
          String username = split[0];
          String password = split[1];
          User user = dbAccess.DBGetUser(username,password);
          System.out.println(user.getUsername());
          String stringSerialized = gson.toJson(user);
          byte[] bytes = sendStatus("SUCCCES", stringSerialized);
          outputStream.write(bytes,0,bytes.length);
          break;
        }
        case "WATERNOW":
        {
          wSC.sendDownLink(gson.toJson("waternow"));
        }

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

  private void startGetDataThread(String url){
    WebSocketClient wSC= new WebSocketClient(url);


    Thread t= new Thread(()->{
      while(true){
        try {
          sleep(900000);
          //wSC.onOpen(ws.get());

        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
