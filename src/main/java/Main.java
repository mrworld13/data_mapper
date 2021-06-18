import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, SqlToolError {
        Connection connection = JDBConnection.getInstance();

        UserMapper mapper = new UserMapper(connection);

        SqlFile sqlFile = new SqlFile(new File("src/main/resources/Users.sql"));
        sqlFile.setConnection(connection);
        sqlFile.execute();

        long startTime = System.nanoTime();
        User user1 = mapper.findById(1);
        long stopTime = System.nanoTime();
        System.out.printf("Not cached User1 search time: %d ms \n",stopTime - startTime);

        startTime = System.nanoTime();
        User user2 = mapper.findById(1);
        stopTime = System.nanoTime();
        System.out.printf("Cached User1 search time: %d ms \n",stopTime - startTime);

        startTime = System.nanoTime();
        Map<Long, User> all = mapper.findAll();
        stopTime = System.nanoTime();
        System.out.printf("Not cached all Users search time: %d ms \n",stopTime - startTime);

        startTime = System.nanoTime();
        User user4 = mapper.findById(2);
        stopTime = System.nanoTime();
        System.out.printf("Cached User2 search time: %d ms \n",stopTime - startTime);

        startTime = System.nanoTime();
        mapper.delete(2);
        stopTime = System.nanoTime();
        System.out.printf("User2 delete time: %d ms \n",stopTime - startTime);

        startTime = System.nanoTime();
        User user5 = mapper.findById(2);
        stopTime = System.nanoTime();
        System.out.printf("Not Cached User2 search time: %d ms \n",stopTime - startTime);
    }
}
