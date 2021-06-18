import java.sql.*;
import java.sql.Date;
import java.util.*;

public class UserMapper {

    private Map<Long, User> usersIdentityMap = new HashMap<>();
    private boolean findAllTrigger = false;

    private final Connection conn;

    public UserMapper(Connection conn) throws SQLException {
        this.conn = conn;
        createTableIfNotExists(conn);
    }

    public void insert(User user) throws SQLException {
        if (usersIdentityMap.get(user.getId()) != null) {
            throw new RuntimeException("This user already exists");
        } else {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "insert into users_table(login, password, fullName, email, age, phoneNumber) values (?, ?, ?, ?, ?, ?);")) {
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getFullName());
                stmt.setString(4, user.getEmail());
                stmt.setShort(5, user.getAge());
                stmt.setInt(6, user.getPhoneNumber());
                stmt.execute();
            }

            usersIdentityMap.put(user.getId(), user);
        }

    }

    public void update(User user) throws SQLException {
        if (usersIdentityMap.get(user.getId()) != null) {
            usersIdentityMap.put(user.getId(), user);
        } else {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "update users_table set login = ?, password = ?, fullName = ?, email = ?, age = ?, phoneNumber =?  where id = ?;")) {
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getFullName());
                stmt.setString(4, user.getEmail());
                stmt.setShort(5, user.getAge());
                stmt.setInt(6, user.getPhoneNumber());
                stmt.setLong(7, user.getId());
                stmt.execute();
            }
        }
    }

    public void delete(long id) throws SQLException {
        usersIdentityMap.remove(id);

        try (PreparedStatement stmt = conn.prepareStatement(
                "delete from users_table where id = ?;")) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }

    public User findById(long id) throws SQLException {

        if (usersIdentityMap.get(id) != null) {
            return usersIdentityMap.get(id);
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                "select * from users_table where id = ?")) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserBuilder(
                        new User(rs.getString(2), rs.getString(3)))
                        .id(rs.getLong(1))
                        .fullName(rs.getString(4))
                        .email(rs.getString(5))
                        .age(rs.getShort(6))
                        .phone(rs.getInt(7))
                        .build();
            }
        }
        return new UserBuilder(new User("", "")).id(-1L).build();
    }

    public Map<Long, User> findAll() throws SQLException {
        if (findAllTrigger) return usersIdentityMap;

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from users_table");

            while (rs.next()) {
                usersIdentityMap.put(rs.getLong(1), new UserBuilder(
                        new User(rs.getString(2), rs.getString(3)))
                        .id(rs.getLong(1))
                        .fullName(rs.getString(4))
                        .email(rs.getString(5))
                        .age(rs.getShort(6))
                        .phone(rs.getInt(7))
                        .build());
            }
        }
        findAllTrigger = true;
        return usersIdentityMap;
    }

    private void createTableIfNotExists(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("create table if not exists users_table (\n" +
                    "\tid int auto_increment primary key,\n" +
                    "    login varchar(25),\n" +
                    "    password varchar(512), \n" +
                    "    fullName varchar(128), \n" +
                    "    email varchar(128), \n" +
                    "    age smallint, \n" +
                    "    phoneNumber int \n" +
                    ");"
            );
        }
    }
}
