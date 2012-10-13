package ru.maks105fm.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SuccessAuthHandler implements AuthenticationSuccessHandler {
	
	private SimpleGrantedAuthority authPartner = new SimpleGrantedAuthority("ROLE_PARTNER");
	private SimpleGrantedAuthority authAgreePartner = new SimpleGrantedAuthority("ROLE_AGREEPARTNER");
	private SimpleGrantedAuthority authUser = new SimpleGrantedAuthority("ROLE_CLIENT");
	private SimpleGrantedAuthority authAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Authentication authentication) throws IOException,
			ServletException {
		httpServletRequest.getRequestURI();
		if (authentication.getAuthorities().contains(authPartner)) {
			httpServletResponse.sendRedirect("partner");
		} else if (authentication.getAuthorities().contains(authAgreePartner)) {
			httpServletResponse.sendRedirect("partner/agreement");
		} else if (authentication.getAuthorities().contains(authAdmin)) {
			httpServletResponse.sendRedirect("admin");
		} else if (authentication.getAuthorities().contains(authUser)) {
			httpServletResponse.sendRedirect("client");
		} else {
			httpServletResponse.sendRedirect(".");
		}
		
	}

}
