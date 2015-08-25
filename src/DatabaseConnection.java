import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseConnection {

	private Connection conn;

	public DatabaseConnection(String url, String user, String pass) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			System.out.println("DBC ERROR: Unable to connect to database.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("DBC ERROR: Driver not found.");
			e.printStackTrace();
		}
	}

	public DatabaseConnection(String user, String pass) {
		this("jdbc:oracle:thin:testuser/password@localhost", user, pass);
	}

	public ResultSet query(String sql) {
		try {
			return conn.prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			System.out.println("DBC ERROR: Unable to execute query.");
			e.printStackTrace();
		}
		return null;
	}

	public void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("DBC ERROR: Unable to disconnect.");
			e.printStackTrace();
		}
	}
	
	// Useful for debugging. Prints entire result of a query.
	public void debugPrint(String sql) {
		try {
			ResultSet rs = conn.prepareStatement(sql).executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>(
						columns);
				for (int i = 1; i <= columns; ++i) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(row);
			}
			for (Object o : list) {
				System.out.println(o);
			}
		} catch (Exception e) {
			System.out.println("DBC ERROR: Unable to debug print the table.");
			e.printStackTrace();
		}
	}
}