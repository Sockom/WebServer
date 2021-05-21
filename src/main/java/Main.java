

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException
    {
        SensorData sensorData = new SensorData();
        DBAccess test = new DBAccess();
        test.insertSensorDataTodbo(sensorData);
        try
        {
            ServerSocket welcomeSocket = new ServerSocket(6969);
            System.out.println("Serveren er startet");
            WebSocketClient webSocketClient = new WebSocketClient("wss://iotnet.cibicom.dk/app?token=vnoTvwAAABFpb3RuZXQuY2liaWNvbS5kazR2J0Aa7A9xrXbjCeaG1bU=");



            while(true)
            {
                Socket connections = welcomeSocket.accept();
                System.out.println("Klient accepteret");
                SocketHandler socketHandler = new SocketHandler(connections,webSocketClient);
                Thread t1 = new Thread(socketHandler);
                t1.setDaemon(true);
                t1.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
