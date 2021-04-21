import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class class1 {

    public class1() throws SQLException {
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        String dbURL = "jdbc:sqlserver://DESKTOP-P2FRPBU\\user=ItsAl:1433";
        Connection connection = DriverManager.getConnection(dbURL);
        if (connection != null){
            System.out.println("Connected");
        }
        connection.prepareStatement("insert into DimEjer VALUES Kasper");
        connection.close();

    }
}
