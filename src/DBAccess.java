import java.sql.*;

public class DBAccess
{

    public DBAccess() throws SQLException {
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        String dbURL = "jdbc:sqlserver://DESKTOP-ASU6SHH\\MSSQLSERVER;user=Host;password=1234;database=GrowBroDWH";//K:SMPCJNQ Mk:P2FRPBU KH:ASU6SHH
        Connection connection = DriverManager.getConnection(dbURL);
        if (connection != null){
            System.out.println("Connected");
        }

    }

    public double DBGetCurrentData(int userID, int greenhouseid){

      try
      {

        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

        String dbURL = "jdbc:sqlserver://DESKTOP-ASU6SHH\\MSSQLSERVER;user=Host;password=1234;database=GrowBroDWH";//K:SMPCJNQ Mk:P2FRPBU KH:ASU6SHH
        Connection connection = DriverManager.getConnection(dbURL);
        if (connection != null){
          System.out.println("Connected");
        }
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
}
