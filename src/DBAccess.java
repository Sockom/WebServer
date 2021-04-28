import java.sql.*;

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
            String dbURL = "jdbc:sqlserver://DESKTOP-ASU6SHH\\MSSQLSERVER;user=Host;password=1234;database=GrowBroDWH";//K:SMPCJNQ Mk:P2FRPBU KH:ASU6SHH
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(dbURL);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (connection != null){
                System.out.println("Connected");
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

    public int insertSensorDataToStage(SensorData sensorData) {
        Connection connection = getConnection();
        return 1;
    }

}
