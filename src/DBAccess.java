import java.sql.*;
import java.util.List;

public class DBAccess
{
        public DBAccess()  {
            try {
                DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        public Connection getConnection() {
            String dbURL = "jdbc:sqlserver://DESKTOP-P2FRPBU\\MSSQLSERVER;user=Host;password=1234;database=GrowBroDWH";//K:SMPCJNQ Mk:P2FRPBU KH:ASU6SHH
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(dbURL);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return connection;
        }
    public double DBGetCurrentData(int userID, int greenhouseid){

      try
      {
        Connection connection = getConnection();
        PreparedStatement statement = connection
            .prepareStatement("select CO2 from edwh.FactManagement where U_ID = ? and DH_ID = ?");
        statement.setInt(1, userID);
        statement.setInt(2, greenhouseid);

        ResultSet resultSet = statement.executeQuery();
        double CO2 = 0;
        if (resultSet.next())
        {
          CO2 = resultSet.getDouble("CO2");
        }
        return CO2;

      }
        catch (SQLException e)
      {
        e.printStackTrace();
      }
      return -1;
    }
    public User DBGetUser(String username,String password)
    {
      try
      {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("Select * from edwh.DimEjer where Username=? and Password=?");
        statement.setString(1,username);
        statement.setString(2,password);

        ResultSet resultSet = statement.executeQuery();
        User user;
        int userId = 0;
        String usernameFromDB = "";
        String passwordFromDB = "";
        if (resultSet.next())
        {
          userId = resultSet.getInt("UserID");
          usernameFromDB = resultSet.getString("Username");
          passwordFromDB = resultSet.getString("Password");
        }
        user = new User(userId,usernameFromDB,passwordFromDB);
        return user;
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      return null;
    }


    public int insertSensorDataToStage(SensorData sensorData) {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection
            .prepareStatement("INSERT INTO stage.FactManagement (DrivhusID, [Time], CO2, Temperatur, Fugtighed, UserID, PlanteID) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
            for( int i = 0 ; i < sensorData.getNumberOf() ; i++){
            statement.setInt(1, sensorData.getDrivhusID());
            statement.setTimestamp(2, sensorData.getTimestamps()[i]);
            statement.setInt(3, sensorData.getCO2()[i]);
            statement.setInt(4, sensorData.getTemperatur()[i]);
            statement.setInt(5, sensorData.getLuftfugtighed()[i]);
            statement.setInt(6, getUserID(sensorData.getDrivhusID()));
            statement.setInt(7, getPlantID(sensorData.getDrivhusID()));
            statement.execute();}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 1;}


    public int getUserID(int drivhusID){
            Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
        .prepareStatement("select UserID from dbo.Drivhus where DrivhusID = ?");
            statement.setInt(1, drivhusID);
            ResultSet r = statement.executeQuery();
            int tal = 0;
            if(r.next()){
               tal = r.getInt("UserID");
            }
            return tal;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }


    public int getPlantID(int drivhusID){
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("select PlanteID from dbo.Plante where DrivhusID = ?");
            statement.setInt(1, drivhusID);
            ResultSet r = statement.executeQuery();
            int tal = 0;
            if(r.next()){
                tal = r.getInt("PlanteID");
            }
            return tal;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
}
