import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class Servlet extends HttpServlet{
	Connection con;
	PreparedStatement pstmt1;
	PreparedStatement pstmt2;
	PreparedStatement pstmt3;
	PreparedStatement pstmt4;
	static String username = "";
	String password = "";
	int mobilequantity;
	int clothesquantity;
	String clothes;
	String mobile;
	ResultSet clothValueRS;
	ResultSet mobileValueRS;
	static int clothValue;
	static int mobileValue;
	int totalClothValue;
	int totalMobileValue;
	String sex = "";
	String email = "";
	static int totalValue;
	String contact = "";
	int result;
	String registerqry = "insert into jejw16.account VALUES (?,?,?,?,?)";
	String loginqry = "select * from jejw16.account where username=? and password=?";
	String fetchqry = "select price from jejw16.stock where product=?";
public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	PrintWriter out = resp.getWriter();
	try {
	Class.forName("com.mysql.jdbc.Driver");
	con = DriverManager.getConnection("jdbc:mysql://localhost:3306?user=root&password=dinga");
	pstmt1 = con.prepareStatement(loginqry);
	pstmt2 = con.prepareStatement(registerqry);
	String hiddenValue = req.getParameter("hiddenValue");
	if(hiddenValue.equalsIgnoreCase("login")) {
		username = req.getParameter("username");
		password = req.getParameter("password");
		pstmt1.setString(1, username);
		pstmt1.setString(2, password);
		ResultSet result = pstmt1.executeQuery();
		if(result.next()) {
			resp.sendRedirect("./order.html");
		}else {
			RequestDispatcher rd = req.getRequestDispatcher("login.html");
			rd.include(req,resp);
			out.println("Invalid Credentials");
		}
	}
	if(hiddenValue.equalsIgnoreCase("register")) {
		username = req.getParameter("username");
		password = req.getParameter("password");
		sex = req.getParameter("sex");
		email = req.getParameter("email");
		contact = req.getParameter("contact");
		pstmt2.setString(1, username);
		pstmt2.setString(2, password);
		pstmt2.setString(3, sex);
		pstmt2.setString(4, email);
		pstmt2.setString(5, contact);
		try {
		result = pstmt2.executeUpdate();
		}
		catch(Exception e) {
			RequestDispatcher rd = req.getRequestDispatcher("register.html");
			rd.include(req,resp);
			out.println("OOPS registration failed.");
			out.println("<html><body></br></body></html>");
			out.println("Possible reasons:");
			out.println("<html><body></br></body></html>");
			out.println("1) You left any field empty");
			out.println("<html><body></br></body></html>");
			out.println("2) Your username is already taken! Try another");
			
		}
		if(result >= 1) {
			RequestDispatcher rd = req.getRequestDispatcher("login.html");
			rd.include(req,resp);
			out.println("Registration successful");
		}
	}
	if(hiddenValue.equalsIgnoreCase("order")) {
		mobilequantity = Integer.parseInt(req.getParameter("mobilequantity"));
		clothesquantity = Integer.parseInt(req.getParameter("clothesquantity"));
		clothes = req.getParameter("clothes");
		mobile = req.getParameter("mobile");
		pstmt3 = con.prepareStatement(fetchqry);
		pstmt4 = con.prepareStatement(fetchqry);
		pstmt3.setString(1, clothes);
		pstmt4.setString(1, mobile);
		System.out.println(clothes);
		System.out.println(mobile);
		clothValueRS = pstmt3.executeQuery();
		mobileValueRS = pstmt4.executeQuery();
	
		
		if(clothValueRS.next()) {
			clothValue = clothValueRS.getInt("price");
		}
		if(mobileValueRS.next()) {
			mobileValue = mobileValueRS.getInt("price");
		}
		
		req.setAttribute("username", username);
		req.setAttribute("mobilevalue", mobileValue);
		req.setAttribute("clothvalue", clothValue);
		
		totalClothValue = clothesquantity*clothValue;
		totalMobileValue = mobilequantity*mobileValue;
		totalValue = totalClothValue + totalMobileValue;
		RequestDispatcher rd = req.getRequestDispatcher("./price.jsp");
		rd.include(req, resp);
				
	}
	if(hiddenValue.equalsIgnoreCase("confirm")) {
		out.println("<html>\r\n" + 
				"<body>\r\n" + 
				"<form action=\"ty.html\">\r\n" + 
				"Press confirm to confirm your details\r\n" + 
				"<input type=\"submit\" value=\"Confirm\">\r\n" + 
				"</form>\r\n" + 
				"</body>\r\n" + 
				"</html>");
		out.println("Your total bill is "+totalValue);
	}
	}
	catch(Exception e) {
		System.out.println("Exception caught");
	}
}
}