import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class class1 {

    public class1() throws SQLException {
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        String dbURL = "jdbc:sqlserver://DESKTOP-P2FRPBU\\MSSQLSERVER;user=Host;password=1234;database=GrowBroDWH";
        Connection connection = DriverManager.getConnection(dbURL);
        if (connection != null){
            System.out.println("Connected");
        }
        connection.prepareStatement("insert into stage.DimEjer " + "VALUES ('Kasper','12345')").execute();
        connection.close();
        System.out.println("Connection closed");

    }
}
