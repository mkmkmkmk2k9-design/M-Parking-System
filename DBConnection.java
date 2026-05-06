import java.sql.Connection; // Thư viện đại diện cho kết nối giữa ứng dụng Java và cơ sở dữ liệu.
import java.sql.DriverManager; // Thư viện dùng để lấy kết nối tới cơ sở dữ liệu thông qua JDBC driver.

public class DBConnection {
	public static Connection getConnection() {
		try {
			  String url = "jdbc:sqlserver://LAPTOP-0UIQI6MQ:1433;"
					     + "databaseName=DA;"
					     + "encrypt=true;"
					     + "trustServerCertificate=true;";
			  String user = "admin";
			  String pass = "123";
		return DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		} return null;
	}
}