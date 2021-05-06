package com.epam;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "run")
public class AppRunner extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		HttpSession session = req.getSession();
		//session.setMaxInactiveInterval(30);
		PrintWriter out = resp.getWriter();

		if (req.getParameter("name") != null) {
			session.setAttribute("name", req.getParameter("name"));
		}
		
		Integer counter = (Integer)session.getAttribute("counter");
		if (counter == null) {
			counter = new Integer(0);
		}
		counter++;
		session.setAttribute("counter",counter);
		out.append("<p>Counter = " + counter + "</p>");

		if (session.getAttribute("name") != null) {
			out.append("<h1>Hello, " + session.getAttribute("name") + "</h1>");
		}
		
		String url = req.getRequestURL().toString();
		out.append("<p> " + url);

		out.append("<form action=\"" + resp.encodeUrl(url) + "\">"); //to work with cookies
		if (session.getAttribute("name") == null) {
			out.append("Enter your name<input type=\"text\" name=\"name\"/>");
		}
		out.append("<input type=\"submit\" /></form>");
		
		
		// this part of code should be before getWriter part!
		int cntr = 0;
		Cookie[] cookies = req.getCookies();
		for (Cookie c : cookies) {
			if ("cntr".equals(c.getName())) {
				cntr = Integer.parseInt(c.getValue());
				break;
			}
		}
		cntr++;
		out.append("<p> Cookie counter " + cntr);
		Cookie cookie = new Cookie("cntr", String.valueOf(cntr));
		cookie.setMaxAge(60);
		resp.addCookie(cookie);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
