import java.io.*;
import java.sql.ResultSet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;
	DatabaseConnection dbc;
	
	public void init() throws ServletException {
		dbc = new DatabaseConnection("testdb", "password");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String logout  = request.getParameter("logout");
		if (logout == null) {
			//
		} else if (logout.equals("true")) {
			request.getSession().setAttribute("userID", null);
		}
		String navRight = Utilities.checkLogin(dbc, request);
		request.setAttribute("navRight", navRight); 
		
		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String type  = request.getParameter("type");
		String name  = request.getParameter("name");
		String email = request.getParameter("email");
		String zip   = request.getParameter("zip");

		String successCreate = "<div class=\"alert alert-success\">";
		successCreate += "<strong>Success!</strong> " + "User created succcessfully" +" added!</div>";
		String failCreate = "<div class=\"alert alert-danger\">";
		failCreate += "<strong>Error!</strong> Invalid data entered.</div>";
		
		String successLogin = "<div class=\"alert alert-success\">";
		successLogin += "<strong>Success!</strong> " + "Log In Successful!" +" </div>";
		String failLogin = "<div class=\"alert alert-danger\">";
		failLogin += "<strong>Error!</strong> Wrong username/password.</div>";
		
		if (type == null || type.equals("")) {
			// do nothing
		} else if (type.equals("1")) {
			if (signup(name, email, zip)) {
				request.setAttribute("feedback", successCreate); 
			} else {
				request.setAttribute("feedback", failCreate); 
			}
		} else if (type.equals("2")) {
			String id = login(name, zip);
			if (id == null || id.equals("")) {
				request.setAttribute("feedback", failLogin); 
			} else {
				HttpSession s = request.getSession();
				s.setAttribute("userID", id);
				request.setAttribute("feedback", successLogin); 
			}
		}
		
		String navRight = Utilities.checkLogin(dbc, request);
		request.setAttribute("navRight", navRight); 
		
		//request.setAttribute("results", result); 
		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	}
	
	private boolean hasNull(String name, String email, String zip) {
		if (name == null  || name.equals("")) { return true; }
		if (email == null || email.equals("")) { return true; }
		if (zip == null   || zip.equals("")) { return true; }
		return false;
	}
	
	private boolean hasNull(String name, String zip) {
		if (name == null  || name.equals("")) { return true; }
		if (zip == null   || zip.equals("")) { return true; }
		return false;
	}
	
	// Generate next unique ID for the given table
	private int nextID(String table) {
		try {
			ResultSet rs = dbc.query("SELECT MAX(id) FROM " + table);
			rs.next();
			return rs.getInt(1) + 1;
		} catch (Exception e) {
			return (int)(Math.random() * 1000);
		}
	}
	
	private boolean signup(String name, String email, String zip) {
		if (hasNull(name, email, zip)) { return false; }
		int id = nextID("users");
		dbc.query(
			" INSERT INTO users (id, name, zip, email)" +
			" VALUES (" + id + ", '" + name + "', " + zip + ", '" + email + "')"
		);
		return true;
	}
	
	private String login(String name, String zip) {
		if (hasNull(name, zip)) { return ""; }
		ResultSet rs = dbc.query(
			"SELECT * FROM users WHERE name = '" + name + "'"
		);
		try {
			if (rs.next()) {
				if (zip.equals(rs.getString("zip"))) {
					return rs.getString("id");
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}	
	
	public void destroy() { 
		dbc.disconnect();
	}
}







