import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class class1 {

    public class1() throws SQLException {
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        String dbURL = "jdbc:sqlserver://DESKTOP-P2FRPBU\\MSSQLSERVER;user=Host;password=1234";
        Connection connection = DriverManager.getConnection(dbURL);
        if (connection != null){
            System.out.println("Connected");
        }

        connection.prepareStatement("insert into stage.DimEjer " + "VALUES ('Kaspers')").execute();
        connection.close();

    }
}
