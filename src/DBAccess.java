import java.sql.*;
import java.util.List;

@SuppressWarnings("SqlDialectInspection")
public class DBAccess
{
        public DBAccess()  {
            try {
                DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
// Start of establishing connection

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


//End of establishing connection


// Start of taking in information from IOT and Android


    public int insertSensorDataTodbo(SensorData sensorData) {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection
            .prepareStatement("INSERT INTO dbo.Drivhus (DrivhusID, [Time], CO2, Temperatur, Fugtighed) VALUES ( ?, ?, ?, ?, ?)");
            for( int i = 0 ; i < sensorData.getNumberOf() ; i++){
            statement.setInt(1, sensorData.getDrivhusID());
            statement.setTimestamp(2, sensorData.getTimestamps()[i]);
            statement.setFloat(3, sensorData.getCO2()[i]);
            statement.setFloat(4, sensorData.getTemperatur()[i]);
            statement.setFloat(5, sensorData.getLuftfugtighed()[i]);
            statement.execute();
                if(updateStage() == 1) {
                    updateEDWH();
                }
            }
        } catch (SQLException throwables) {
            return -1;
        }
        return 1;}



    public int insertGreenhouseToDB(Greenhouse greenhouse){
            Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
            .prepareStatement("INSERT INTO dbo.Drivhus (Navn, UserID) VALUES ( ?, ?)");
            statement.setString(1, greenhouse.getName());
            statement.setInt(2, greenhouse.getUserID());
            statement.execute();
            if(updateStage() == 1) {
                updateEDWH();
            }
        } catch (SQLException throwables) {
            return -1;
        }
        return 1;
    }

    public int insertPlantToDB(Plant plant){
            Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
            .prepareStatement("INSERT INTO dbo.Plant (Navn, DrivhusID) VALUES ( ?, ?)");
            statement.setString(1, plant.getName());
            statement.setInt(2, plant.getDrivhusID());
            statement.execute();
            if(updateStage() == 1) {
                updateEDWH();
            }
        } catch (SQLException throwables) {
            return -1;
        }
        return 1;
    }

    public int insertUserToDB(User user) {
            Connection connection = getConnection();
            PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("INSERT INTO dbo.Ejer (Username, Password) VALUES ( ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.execute();
            if(updateStage() == 1) {
                updateEDWH();
            }
        } catch (SQLException throwables) {
            return -1;
        }
      return 1;
    }

    //Start of Updaters on dbo


    public int updatePlant(Plant plant){
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("INSERT INTO dbo.plant (Navn, PlanteScore, TemperaturKrav, CO2Krav, FugtighedsKrav VALUES (?, ?, ?, ?, ?) WHERE (PlanteID = ?) )");
            statement.setString(1,plant.getName());
            statement.setFloat(2, plant.getPlantScore());
            statement.setFloat(3, plant.getTemperatureRequirement());
            statement.setFloat(4, plant.getCO2Requirement());
            statement.setFloat(5, plant.getHumidityRequirement());
            statement.setInt(6, plant.getPlantID());
            statement.execute();
            if(updateStage() == 1) {
                updateEDWH();
            }
        } catch (SQLException throwables) {
            return -1;
        }
        return 1;

    }
    public int updateUser(User user) {
            Connection connection = getConnection();
            PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("INSERT INTO dbo.Ejer (Username, Password) VALUES ( ?, ?) WHERE (UserID = ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());
            statement.execute();
            if(updateStage() == 1) {
                updateEDWH();
            }
        } catch (SQLException throwables) {
            return -1;
        }
        return 1;
    }

    public int updateGreenhouse(Greenhouse greenhouse) {
            Connection connection = getConnection();
            PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("INSERT INTO dbo.Drivhus (Navn, UserID) VALUES ( ?, ?) WHERE (DrivhusID = ?)");
            statement.setString(1, greenhouse.getName());
            statement.setInt(2, greenhouse.getUserID());
            statement.setInt(3, greenhouse.getGreenHouseID());
            statement.execute();
            if(updateStage() == 1){
            updateEDWH();
            }
        } catch (SQLException throwables) {
            return -1;
        }
        return 1;
    }


    // End of taking in information from IOT and Android


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


// SQL Script managers
private int updateStage() {
    Connection connection = getConnection();
    PreparedStatement statement = null;
    try {
        statement = connection
                .prepareStatement("\n" +
                        "truncate table stage.DimDrivhus\n" +
                        "insert into stage.DimDrivhus\n" +
                        "([DrivhusID]\n" +
                        "      ,[Navn])\n" +
                        "SELECT [DrivhusID]\n" +
                        "      ,[Navn]\n" +
                        "FROM dbo.Drivhus\n" +
                        "\n" +
                        "truncate table stage.DimEjer\n" +
                        "insert into stage.DimEjer\n" +
                        "([UserID]\n" +
                        "      ,[Username])\n" +
                        "SELECT [UserID]\n" +
                        "      ,[Username]\n" +
                        "FROM dbo.Ejer\n" +
                        "\n" +
                        "truncate table stage.DimPlante\n" +
                        "insert into stage.DimPlante\n" +
                        "([PlanteID]\n" +
                        "      ,[Navn]\n" +
                        "      ,[PlanteScore]\n" +
                        "      ,[TemperaturKrav]\n" +
                        "      ,[CO2Krav]\n" +
                        "      ,[FugtighedsKrav])\n" +
                        "SELECT [PlanteID]\n" +
                        "      ,[Navn]\n" +
                        "      ,ISNULL([PlanteScore], -1) as [PlanteScore]\n" +
                        "      ,ISNULL([TemperaturKrav], -1) as [TemperaturKrav]\n" +
                        "      ,ISNULL([CO2Krav], -1) as [CO2Krav]\n" +
                        "      ,ISNULL([FugtighedsKrav], -1) as [FugtighedsKrav]\n" +
                        "FROM dbo.Plante\n" +
                        "\n" +
                        "truncate table stage.FactManagement\n" +
                        "insert into stage.FactManagement\n" +
                        "([UserID]\n" +
                        "      ,[DrivhusID]\n" +
                        "      ,[PlanteID]\n" +
                        "      ,[Temperatur]\n" +
                        "      ,[CO2]\n" +
                        "      ,[Fugtighed]\n" +
                        "      ,[Time])\n" +
                        "SELECT e.[UserID]\n" +
                        "      ,p.[DrivhusID]\n" +
                        "      ,[PlanteID]\n" +
                        "      ,[Temperatur]\n" +
                        "      ,[CO2]\n" +
                        "      ,[Fugtighed]\n" +
                        "      ,[Time]\n" +
                        "FROM dbo.Drivhus dh\n" +
                        "inner join dbo.ejer e\n" +
                        "on dh.UserID = e.UserID\n" +
                        "inner join dbo.Plante p\n" +
                        "on p.DrivhusID = dh.DrivhusID");
        statement.execute();
    } catch (SQLException throwables) {
        return -1;
    }
    return 1;
    }


private int updateEDWH(){
    Connection connection = getConnection();
    PreparedStatement statement = null;
    try {
        statement = connection
                .prepareStatement("use GrowBroDWH\n" +
                        "\n" +
                        "insert into edwh.DimDrivhus\n" +
                        "([DrivhusID]\n" +
                        "      ,[Navn])\n" +
                        "SELECT [DrivhusID]\n" +
                        "      ,[Navn]\n" +
                        "FROM stage.DimDrivhus\n" +
                        "\n" +
                        "insert into edwh.DimEjer\n" +
                        "([UserID]\n" +
                        "      ,[Username])\n" +
                        "SELECT [UserID]\n" +
                        "      ,[Username]\n" +
                        "FROM stage.DimEjer\n" +
                        "\n" +
                        "insert into edwh.DimPlante\n" +
                        "([PlanteID]\n" +
                        "      ,[Navn]\n" +
                        "      ,[PlanteScore]\n" +
                        "      ,[TemperaturKrav]\n" +
                        "      ,[CO2Krav]\n" +
                        "      ,[FugtighedKrav])\n" +
                        "SELECT  [PlanteID]\n" +
                        "      ,[Navn]\n" +
                        "      ,[PlanteScore]\n" +
                        "      ,[TemperaturKrav]\n" +
                        "      ,[CO2Krav]\n" +
                        "      ,[FugtighedKrav]\n" +
                        "FROM stage.DimPlante\n" +
                        "\n" +
                        "insert into edwh.FactManagement\n" +
                        "([Temperatur]\n" +
                        "\t  ,[CO2]\n" +
                        "      ,[Fugtighed]\n" +
                        "      ,[Time])\n" +
                        "SELECT [Temperatur]\n" +
                        "      ,[CO2]\n" +
                        "      ,[Fugtighed]\n" +
                        "      ,[Time]\n" +
                        "FROM stage.FactManagement");
        statement.execute();
    } catch (SQLException throwables) {
        return -1;
    }
    return 1;
        }
}