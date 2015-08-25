import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/restlist")
public class Restaurants extends HttpServlet {

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
		//String term  = request.getParameter("search");
		
		request.setAttribute("navRight", Utilities.checkLogin(dbc, request)); 
		
		System.out.println(request.getSession().getAttribute("userID"));
		
		String result = listRestaurants();
		
		request.setAttribute("results", result); 
		getServletContext().getRequestDispatcher("/restaurants.jsp").forward(request, response);
	}
	
	
	private String listRestaurants() {
		String ans = "<thead><tr><th>Name</th><th>Address</th><th>Description</th><th>Number of reviews</th><th>Average Rating</th></thead>";
		ans += "<tbody>";
		
		try {
			ResultSet rs = dbc.query(
				" WITH countReview AS (" +
				"   SELECT restID \"r1\", COUNT(rating) FROM reviews GROUP BY restID" +
				" )," + 
				" avgRating AS (" + 
				"   SELECT restID \"r2\", AVG(rating) FROM reviews GROUP BY restID" +
				" )," + 
				" combined AS (" +
				"   SELECT * FROM countReview c, avgRating a WHERE c.\"r1\" = a.\"r2\"" + 
				" )" + 
				" SELECT * FROM combined c" + 
				" FULL OUTER JOIN restaurants r" +
				" ON c.\"r1\" = r.id"
			);
			while (rs.next()) {
				ans += "<tr>";
				ans += "<td><a href=\"restpage?restID=" + rs.getString("id")
						+ "\">" + rs.getString("name") + "</td>";
				ans += "<td>" + rs.getString("address") + "</td>";
				ans += "<td>" + rs.getString("description") + "</td>";
				ans += "<td>" + noNull(rs.getString("count(rating)")) + "</td>";
				ans += "<td>" + noNull(rs.getString("avg(rating)")) + " / 5</td>";
				ans += "</tr>";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ans + "</tbody>";
	}
	
	private String noNull(String x) {
		if (x == null) {
			return "-";
		} else {
			return x;
		}
	}
	
	public void destroy() { 
		dbc.disconnect();
	}
}







