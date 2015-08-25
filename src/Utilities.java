import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;


public class Utilities {

	private static final String loginButtons = 
	        "<li><a href=\"login\"><span class=\"glyphicon glyphicon-user\"></span> Sign Up</a></li>" + 
	        "<li class=\"active\"><a href=\"login\"><span class=\"glyphicon glyphicon-log-in\"></span> Login</a></li>";
	
	public static String checkLogin(DatabaseConnection dbc, HttpServletRequest request) {
		String userID = (String) request.getSession().getAttribute("userID");
		if (userID == null || userID.equals("")) {
			return loginButtons;
		} else {
			
			String userName = "";
			ResultSet rs = dbc.query("SELECT name FROM users WHERE id = " + userID);
			try {
				if (rs.next()) {
					userName = rs.getString("name");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return formatSignOutButtons(userID, userName);
		}
	}
	
	private static String formatSignOutButtons(String userID, String userName) {
        String acc    = "<li><a href=\"user?userID=" + userID + "\"><span class=\"glyphicon glyphicon-user\"></span> " + userName + "</a></li>";
        String logOut = "<li><a href=\"login?logout=true\"><span class=\"glyphicon glyphicon-log-in\"></span> Sign Out</a></li>";

		return acc + logOut;
	}
	
	public static String reformatDate(String dbDate) {
		try {
			java.util.Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(dbDate);
			return new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").format(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return dbDate;
		}
	}
	
}
