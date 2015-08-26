import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/restpage")
public class Restpage extends HttpServlet {

	private static final long serialVersionUID = 1L;
	DatabaseConnection dbc;
	String restID = "";
	
	public void init() throws ServletException {
		dbc = new DatabaseConnection("testdb", "password");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		response.setContentType("text/html");

		request.setAttribute("navRight", Utilities.checkLogin(dbc, request)); 
		
		restID  = request.getParameter("restID");
		
		String result = getRestInfo(restID);
		
		request.setAttribute("results", result); 
		getServletContext().getRequestDispatcher("/restpage.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("restpage doPost");
		response.setContentType("text/html");
		String rating  = request.getParameter("rating");
		String review  = request.getParameter("review");
		
		String success = "<div class=\"alert alert-success\"><strong>Success!</strong> ";
		String fail = "<div class=\"alert alert-danger\"><strong>Error!</strong> ";
		
		String result = "";
		String loginerror = "";
		String userID = (String) request.getSession().getAttribute("userID");
		if (userID == null || userID.equals("")) {
			loginerror = fail + "Please log in to submit a review!</div>";
		} else if (userAlreadyPosted( userID, restID)) {
			loginerror = fail + "You can only post one review per restaurant!</div>";
		} else {
			loginerror = success + "Review posted successfully!</div>";
			result = insertReview(restID, rating, review);
		}
		
		
		request.setAttribute("loginerror", loginerror); 
		request.setAttribute("results", result); 
		
		getServletContext().getRequestDispatcher("/restpage.jsp").forward(request, response);
		
	}
	
	private boolean userAlreadyPosted(String currentuserID, String restID) {
		boolean alreadyPosted = false;
		
		ResultSet rs = dbc.query("SELECT * FROM reviews WHERE restID=" + restID);
		
		try {
			while (rs.next()) {
			
				String userid = rs.getString("userID");
				if (currentuserID.equals(userid)) {
					alreadyPosted = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return alreadyPosted;
	}
	
	
	private String insertReview(String restID, String rating, String review) {
		System.out.println("insertReview");
		if (rating == null || rating.equals("")) { return ""; }
		if (review == null || review.equals("")) { return ""; }
		System.out.println("not null");
		
		int maxID = 0;
		
		ResultSet rs = dbc.query("select max(id) from reviews");
		try {
			while (rs.next()) {
				maxID = Integer.parseInt(rs.getString("max(id)"));
				System.out.println(maxID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());		
		date = "TO_DATE('" + date + "', 'MM/DD/YYYY')";
		
		String sql = " INSERT INTO reviews (id, userID, restID, rating, review, r_date)" +
				" VALUES (" + (++maxID) + ", 0, " + restID + ", "+rating+", '"+review+ "'," + date + ")";
		
		System.out.println(sql);
		dbc.query(sql);
		
		return "";
	}
	
	
	private String getRestInfo(String restID) {
		if (restID == null || restID.equals("")) { return ""; }
		
		System.out.println("getUserInfo: " + restID);
		
		String ans = "";//"<thead><tr><th>Type</th><th>First Name</th><th>Last Name</th><th>Details</th></thead>";
		
		
		try {
			ResultSet rs = dbc.query(
				"select * from restaurants where id=" + restID
			);
			
			while (rs.next()) {
				ans += "<h1>" + rs.getString("name") + "</h1>";
				ans += "<p><b>Address:  </b>" + rs.getString("address") + "</p>";
				ans += "<p><b>Description:  </b>" + rs.getString("description") + "</p>";
				ans += "<br>";
			}
			
			ans += "<h3>Reviews</h3><table class=\"table table-condensed\" style=\"position:relative; top:-1px;\">";
			
			
			rs = dbc.query(
				" SELECT * FROM reviews, users WHERE restid = " + restID +
				" AND reviews.userID = users.id "
			);
			
			while (rs.next()) {
				ans += "<tr><th>" + rs.getString("name") + "</th>";
				ans += "<th>" + Utilities.reformatDate(rs.getString("r_date")) + "</th></tr>";
				ans += "<tr><td>" + rs.getString("rating") + " / 5 Stars</td>";
				ans += "<td>" + rs.getString("review") + "</td></tr><tr><td> </td><td> </td></tr>";
				ans += "<br/>";
			}
			ans += "</table>";

		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		System.out.println(ans);
		
		return ans;
	}
	
	public void destroy() { 
		dbc.disconnect();
	}
}




