/* Database front end for IoT class project - Code edited by 
  Maj Paul Haagenson and Capt Micah Akin 
  Based on code originally written for Databases class, originally
  provided by Prof. Das */

package edu.nps.muster.gateway;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

public class muster extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();

	//This portion grabs the email from the HTTP GET request
	String email= request.getParameter("email");
	
	try {
		
		boolean didMuster = new musterDatabase().didMusterToday(email);
				
		if (didMuster) {
			//Return success message to the sending .html page
			out.println("You have mustered today.");
		} else {
			out.println("You have NOT mustered today.");
		}
		

    } catch (Exception exp) {
	out.println("Exception = " +exp);
	System.out.println("Exception = " +exp);

    }
	}
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doGet(request, response);
    }
}