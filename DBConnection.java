import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {
    private static Connection conn = null;
    private static String url;
    private static String user;
    private static String pass;

    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            pass = props.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Lỗi cấu hình Database: " + e.getMessage());
        }
    }

    public static synchronized Connection getConnection() {
        try {
            if (conn != null && !conn.isClosed() && conn.isValid(2)) {
                return conn;
            }
            if (url != null) {
                conn = DriverManager.getConnection(url, user, pass);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối MySQL: " + e.getMessage());
        }
        return conn;
    }

    public static synchronized void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
