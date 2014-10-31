package asu.bank.security.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class SurakshitLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
         
//         response.setDateHeader("Expires", 0);

         response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");// HTTP 1.1.
         response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
         response.setHeader("Expires", "Fri, 17 Mar 2010 06:00:00 GMT");
         response.setHeader("Last-Modified", new Date().toString());
         response.setHeader("Pragma", "no-cache");
         
         Cookie cookie = new Cookie("JSESSIONID", null);
         cookie.setPath(request.getContextPath());
         cookie.setMaxAge(0);
         response.addCookie(cookie);
         
         response.sendRedirect("index.jsp"); // No logged-in user found, so redirect to login page.
		
	}

}
