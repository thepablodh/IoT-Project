//Database Final Project balances servlet - Maj Paul Haagenson
//This code is modified from that provided by Prof. Das
/*After modifying local addresses, code should be compiled
	with the resulting .class file placed in the apache servlets
	folder. */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

/* Servlet used update a running balance of a personal expense database
 */

public class balances extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {

// class to write out to the log files

   ServletContext sc = getServletContext();

   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   String docType = "<!DOCTYPE HTML>\n";
   out.println(docType +
                "<HTML>\n" +
                "<HEAD><meta charset='utf-8'><TITLE>Balances</TITLE></HEAD>\n" +
                "<BODY>\n");

// Get the Data from the Database 

    try {

		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Driver loaded");
		
		//MODIFY THESE FOR YOUR CONFIGURATION
		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "paul";
		String pwd = "enterin";

		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);

		Statement query_stmt=DB_mobile_conn.createStatement();
				
		//Print out account balances
		String query="select account_NAME, SUM(expense_amt) from expenses group by account_NAME";
		ResultSet query_rs=query_stmt.executeQuery(query);

		//Prints out the account balances by account name (which is forced on user input)
		while (query_rs.next()) {
    		out.println("<H4> Account: " + query_rs.getString(1) + " ---- $" + query_rs.getString(2) + "</H4>" );
		}
		
		query_rs.close();
		query_stmt.close();
		DB_mobile_conn.close();

    } catch (Exception exp) {
	out.println("Exception = " +exp);
	System.out.println("Exception = " +exp);
    }
    out.println(docType +"</BODY></HTML>");
  }
}
//End of file - Maj Paul Haagenson - SDG