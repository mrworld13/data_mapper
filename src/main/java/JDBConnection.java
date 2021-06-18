import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/users_test_db?createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";

    private static Connection connection = null;

    private JDBConnection () {
    }

    public static Connection getInstance() {
        if (connection == null) {
            try{
                connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't initialize DB");
            }
        }
        return connection;
    }
}
