import com.google.gson.Gson;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import java.util.ArrayList;

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
          wSC.sendDownLink(gson.toJson("waternow") +":"+message.getJson());
          break;
        }
        case "GETGREENHOUSES":
        {
          int userID = Integer.parseInt(message.getJson());
          List<Greenhouse> greenhouses = dbAccess.getGreenhouses(userID);
          String stringSerialized = gson.toJson(greenhouses);
          byte[] bytes = sendStatus("SUCCES",stringSerialized);
          outputStream.write(bytes,0,bytes.length);
          break;
        }
        case "GETGREENHOUSEBYID":
        {
          String[] split = message.getJson().split(":");
          int userID = Integer.parseInt(split[0]);
          int greenHouseID = Integer.parseInt(split[1]);
          Greenhouse greenhouse = dbAccess.getGreenHouseByID(userID,greenHouseID);
          String stringSerialized = gson.toJson(greenhouse);
          byte[] bytes = sendStatus("SUCCES",stringSerialized);
          outputStream.write(bytes,0,bytes.length);
          break;
        }
        case "GETAVERAGEDATA":
        {
          String[] split = message.getJson().split(":");
          int userID = Integer.parseInt(split[0]);
          int greenHouseID = Integer.parseInt(split[1]);
          /*
          *
          *
          *
          *
          * getAverageData er ikke færdig på DBAccess
          *
          *
          *
          *  */
          ApiCurrentDataPackage apiCurrentDataPackage = dbAccess.getAverageData(userID,greenHouseID);
          String stringSerialized = gson.toJson(apiCurrentDataPackage);
          byte[] bytes = sendStatus("SUCCES",stringSerialized);
          outputStream.write(bytes,0,bytes.length);
          break;
        }
        case "OPENWINDOW":
        {
          wSC.sendDownLink(gson.toJson("openwindow")+":"+message.getJson());
          break;
        }
        case "ADDGREENHOUSE":
        {
          Greenhouse greenhouse = gson.fromJson(message.getJson(), Greenhouse.class);
          int greenHouseID = dbAccess.insertGreenhouseToDB(greenhouse);
          String stringSerialized = gson.toJson(greenHouseID);
          byte[] bytes = sendStatus("SUCCES", stringSerialized);
          outputStream.write(bytes, 0, bytes.length);
          break;
        }
        case "ADDPLANT":
        {
          Plant plant = gson.fromJson(message.getJson(),Plant.class);
          int plantId = dbAccess.insertPlantToDB(plant);
          String stringSerialized = gson.toJson(plantId);
          byte[] bytes = sendStatus("SUCCES", stringSerialized);
          outputStream.write(bytes, 0, bytes.length);
          break;
        }
        case "ADDUSER":
        {
          User user = gson.fromJson(message.getJson(),User.class);
          int userID = dbAccess.insertUserToDB(user);
          String stringSerialized = gson.toJson(userID);
          byte[] bytes = sendStatus("SUCCES", stringSerialized);
          outputStream.write(bytes, 0, bytes.length);
          break;
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
