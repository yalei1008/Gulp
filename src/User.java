import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/user")
public class User extends HttpServlet {

	private static final long serialVersionUID = 1L;
	DatabaseConnection dbc;
	
	public void init() throws ServletException {
		dbc = new DatabaseConnection("testdb", "password");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		request.setAttribute("navRight", Utilities.checkLogin(dbc, request)); 
		
		String userID  = request.getParameter("userID");
		
		String result = getUserInfo(userID);
		
		request.setAttribute("results", result); 
		getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
	}
	
	
	private String getUserInfo(String userID) {
		if (userID == null || userID.equals("")) { return ""; }
		
		System.out.println("getUserInfo: " + userID);
		
		String ans = "";//"<thead><tr><th>Type</th><th>First Name</th><th>Last Name</th><th>Details</th></thead>";
		ans += "<tbody>";
		
		try {
			ResultSet rs = dbc.query(
				"SELECT * FROM users WHERE id = " + userID
			);
			dbc.debugPrint("SELECT * FROM users WHERE id = " + userID);
			while (rs.next()) {
				ans += "<tr>";
				ans += "<td>" + rs.getString("name") + "</td>";
				ans += "<td>" + rs.getString("zip") + "</td>";
				ans += "<td>" + rs.getString("email") + "</td>";
				ans += "</tr>";
			}
			rs = dbc.query(
				" SELECT * FROM reviews, restaurants WHERE userID = " + userID +
				" AND reviews.restID = restaurants.id "
			);
			while (rs.next()) {
				ans += "<tr>";
				ans += "<td>" + rs.getString("name") + "</td>";
				ans += "<td>" + rs.getString("rating") + "</td>";
				ans += "<td>" + rs.getString("review") + "</td>";
				ans += "</tr>";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ans + "</tbody>";
	}
	
	public void destroy() { 
		dbc.disconnect();
	}
}







