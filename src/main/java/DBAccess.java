
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
            String dbURL = "jdbc:sqlserver://growbro.cdkppreaz70m.us-east-2.rds.amazonaws.com;databaseName=GrowBroDWH;user=admin;password=adminadmin";//K:SMPCJNQ Mk:P2FRPBU KH:ASU6SHH MB:HFHHMQP
            Connection connection = null;
            try {
                while (connection==null) {
                    System.out.println("Connecting....");
                    connection = DriverManager.getConnection(dbURL);

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return connection;
        }
    public ApiCurrentDataPackage DBGetCurrentData(int userID, int greenhouseid){

      try
      {
        Connection connection = getConnection();
        PreparedStatement statement = connection
            .prepareStatement("select * from edwh.FactManagement where U_ID = ? and DH_ID = ?");
        statement.setInt(1, userID);
        statement.setInt(2, greenhouseid);

        ResultSet resultSet = statement.executeQuery();
        double CO2 = 0;
        double temperatur = 0;
        double fugtighed = 0;
        String d_id = null;
        Timestamp time = null;
        if (resultSet.next())
        {
          CO2 = resultSet.getDouble("CO2");
          temperatur = resultSet.getDouble("Temperatur");
          fugtighed = resultSet.getDouble("Fugtighed");
          d_id = resultSet.getString("D_ID");
        }
        statement = connection.prepareStatement("select Date from edwh.DimDate where D_ID = ?");
        statement.setString(1,d_id);
        ResultSet resultSet1 = statement.executeQuery();
        if (resultSet1.next())
        {
          time = resultSet1.getTimestamp("Date");
        }
        DataContainer CO2data = new DataContainer(CO2,DataType.CO2);
        DataContainer temperaturData = new DataContainer(temperatur,DataType.TEMPERATURE);
        DataContainer fugtighedData = new DataContainer(fugtighed,DataType.HUMIDITY);
        List<DataContainer> list = new ArrayList<>();
        list.add(CO2data);
        list.add(temperaturData);
        list.add(fugtighedData);
        ApiCurrentDataPackage apiCurrentDataPackage = new ApiCurrentDataPackage(list,time);
        return apiCurrentDataPackage;
      }
        catch (SQLException e)
      {
        e.printStackTrace();
      }
      return null;
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
        if (user.getPassword().equals(password)) {
            return user;
        }
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

    public int insertSensorDataTodbo(int id,int co2,int humidity,int temperature){
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Drivhus SET [Time]=?, CO2 = ?, Temperatur = ?, Fugtighed = ? WHERE DrivhusID =? ");
            System.out.println("inserting data to DB");
            statement.setInt(5,id);
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setDouble(2, co2);
            statement.setDouble(3, temperature);
            statement.setDouble(4, humidity);
            statement.execute();

            } catch (SQLException e) {
            return -1;
        };
        return 1;
    }



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
            statement = connection.prepareStatement("select DrivhusID from dbo.Drivhus where Navn = ? and UserID = ?");
            statement.setString(1,greenhouse.getName());
            statement.setInt(2,greenhouse.getUserID());

            ResultSet resultSet = statement.executeQuery();
            int greenhouseID = 0;
            while (resultSet.next())
            {
              greenhouseID = resultSet.getInt("DrivhusID");
            }
            return greenhouseID;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public int insertPlantToDB(Plant plant){
            Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
            .prepareStatement("INSERT INTO dbo.Plante (Navn, DrivhusID) VALUES ( ?, ?)");
            statement.setString(1, plant.getName());
            statement.setInt(2, plant.getGreenHouseID());
            statement.execute();
            if(updateStage() == 1) {
                updateEDWH();
            }
          statement = connection.prepareStatement("select PlanteID from dbo.Plante where Navn = ? and DrivhusID = ?");
          statement.setString(1,plant.getName());
          statement.setInt(2,plant.getGreenHouseID());

          ResultSet resultSet = statement.executeQuery();
            int plantID = 0;
            while (resultSet.next())
            {
              plantID = resultSet.getInt("PlanteID");
            }
            return plantID;
        } catch (SQLException throwables) {
           throwables.printStackTrace();
        }
        return -1;
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
          statement = connection.prepareStatement("select UserID from dbo.Ejer where Username = ? and Password = ?");
          statement.setString(1,user.getUsername());
          statement.setString(2,user.getPassword());

          ResultSet resultSet = statement.executeQuery();
            int userID = 0;
            while (resultSet.next())
            {
              userID = resultSet.getInt("UserID");
            }
            System.out.println(userID);
            return userID;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

  public List<Greenhouse> getGreenhouses(int userID)
  {
    Connection connection = getConnection();
    PreparedStatement statement = null;
    try {
      statement = connection
          .prepareStatement("select * from dbo.Drivhus where UserID = ?");
      statement.setInt(1,userID );
      ResultSet r = statement.executeQuery();
      List<Greenhouse> greenhouses = new ArrayList<>();
      Greenhouse greenhouse = null;
      while (r.next()){
        greenhouse = new Greenhouse();
        greenhouse.setGreenHouseID(r.getInt("DrivhusID"));
        greenhouse.setName(r.getString("Navn"));
        greenhouse.setUserID(r.getInt("UserID"));
        SensorDataFromAndroid co2 = new SensorDataFromAndroid("CO2",r.getDouble("CO2"));
        SensorDataFromAndroid temperature = new SensorDataFromAndroid("Temperature",r.getDouble("Temperatur"));
        SensorDataFromAndroid fugtighed = new SensorDataFromAndroid("Humidity",r.getDouble("Fugtighed"));
        greenhouse.getSensorData().add(co2);
        greenhouse.getSensorData().add(temperature);
        greenhouse.getSensorData().add(fugtighed);
        greenhouses.add(greenhouse);
      }
      return greenhouses;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public Greenhouse getGreenHouseByID(int userID,int greenHouseID)
  {
    Connection connection = getConnection();
    PreparedStatement statement = null;
    try {
      statement = connection
          .prepareStatement("select * from dbo.Drivhus where DrivhusID = ? and UserID = ?");
      statement.setInt(1, greenHouseID);
      statement.setInt(2,userID);
      ResultSet r = statement.executeQuery();
      Greenhouse greenhouse = null;
      if(r.next()){
        greenhouse = new Greenhouse();
        greenhouse.setGreenHouseID(r.getInt("DrivhusID"));
        greenhouse.setName(r.getString("Navn"));
        greenhouse.setUserID(r.getInt("UserID"));
        SensorDataFromAndroid co2 = new SensorDataFromAndroid("CO2",r.getDouble("CO2"));
        SensorDataFromAndroid temperature = new SensorDataFromAndroid("Temperature",r.getDouble("Temperatur"));
        SensorDataFromAndroid fugtighed = new SensorDataFromAndroid("Humidity",r.getDouble("Fugtighed"));
        greenhouse.getSensorData().add(co2);
        greenhouse.getSensorData().add(temperature);
        greenhouse.getSensorData().add(fugtighed);
      }
      return greenhouse;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public ApiCurrentDataPackage getAverageData(int userID,int greenHouseID)
  {
    try
    {
      Connection connection = getConnection();
      PreparedStatement statement = connection
          .prepareStatement("select * from edwh.FactManagement where U_ID = ? and DH_ID = ?");
      statement.setInt(1, userID);
      statement.setInt(2, greenHouseID);

      ResultSet resultSet = statement.executeQuery();
      double CO2 = 0;
      double temperatur = 0;
      double fugtighed = 0;
      String D_ID = null;
      DateTimeFormatter time = null;
      List<DataContainer> list = new ArrayList<>();
      while (resultSet.next())
      {
        CO2 = resultSet.getDouble("CO2");
        temperatur = resultSet.getDouble("Temperatur");
        fugtighed = resultSet.getDouble("Fugtighed");
        D_ID = resultSet.getString("D_ID");
        DataContainer CO2data = new DataContainer(CO2,DataType.CO2);
        DataContainer temperaturData = new DataContainer(temperatur,DataType.TEMPERATURE);
        DataContainer fugtighedData = new DataContainer(fugtighed,DataType.HUMIDITY);
        list.add(CO2data);
        list.add(temperaturData);
        list.add(fugtighedData);
      }

      ApiCurrentDataPackage apiCurrentDataPackage = new ApiCurrentDataPackage(list,null);
      return apiCurrentDataPackage;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return null;
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
                .prepareStatement("use [GrowBroDWH]\n" +
                        "\n" +
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
                        "      ,dh.[Time]\n" +
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
                        "      ,[FugtighedsKrav])\n" +
                        "SELECT  [PlanteID]\n" +
                        "      ,[Navn]\n" +
                        "      ,[PlanteScore]\n" +
                        "      ,[TemperaturKrav]\n" +
                        "      ,[CO2Krav]\n" +
                        "      ,[FugtighedsKrav]\n" +
                        "FROM stage.DimPlante\n" +
                        "\n" +
                        "insert into edwh.FactManagement\n" +
                        "           ([DH_ID]\n" +
                        "           ,[U_ID]\n" +
                        "           ,[P_ID]\n" +
                        "           ,[D_ID]\n" +
                        "           ,[Temperatur]\n" +
                        "           ,[CO2]\n" +
                        "           ,[Fugtighed])\n" +
                        "SELECT DH.[DH_ID]\n" +
                        "           ,E.[U_ID]\n" +
                        "           ,P.[P_ID]\n" +
                        "           ,D.[D_ID]\n" +
                        "           ,F.[Temperatur]\n" +
                        "           ,F.[CO2]\n" +
                        "           ,F.[Fugtighed]\n" +
                        "FROM stage.FactManagement as f\n" +
                        "inner join [edwh].DimDrivhus as dh \n" +
                        "on DH.DrivhusID = f.DrivhusID\n" +
                        "inner join [edwh].DimEjer as e \n" +
                        "on E.UserID = f.UserID\n" +
                        "inner join [edwh].DimPlante as p \n" +
                        "on p.PlanteID = f.PlanteID\n" +
                        "inner join [edwh].DimDate as d \n" +
                        "on d.[Date] = f.[Time]");
        statement.execute();
    } catch (SQLException throwables) {
        return -1;
    }
    return 1;
        }

        public List<Integer> getWaterNowAndWindowIsOpen()
        {
          ArrayList<Integer> list = new ArrayList<>();
          Connection connection = getConnection();
          PreparedStatement statement = null;
          try {
            statement = connection
                .prepareStatement("select * from dbo.Drivhus");
            ResultSet r = statement.executeQuery();
            int windowIsOpen = 0;
            int waterNow = 0;
            if(r.next()){
              windowIsOpen = r.getInt("WindowIsOpen");
              waterNow = r.getInt("WaterNow");
            }
            list.add(windowIsOpen);
            list.add(waterNow);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
          setWaterNow();
          return list;
        }

        private void setWaterNow()
        {
          Connection connection = getConnection();
          PreparedStatement statement = null;
          try {
            statement = connection
                .prepareStatement("update dbo.Drivhus set WaterNow = 0 where WaterNow = 1");
            statement.executeQuery();
            }
          catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        }
}