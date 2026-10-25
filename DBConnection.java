
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_db";

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "root");

    public static Connection getConnection() throws SQLException {

        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new SQLException(
                "Set the DB_PASSWORD environment variable first."
            );
        }

        return DriverManager.getConnection(URL, USER, password);
    }
}