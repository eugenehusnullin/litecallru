package ru.maks105fm.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SuccessAuthHandler implements AuthenticationSuccessHandler {
	
	private SimpleGrantedAuthority sgaPartner = new SimpleGrantedAuthority("ROLE_PARTNER");
	private SimpleGrantedAuthority sgaUser = new SimpleGrantedAuthority("ROLE_USER");

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Authentication authentication) throws IOException,
			ServletException {
		httpServletRequest.getRequestURI();
		if (authentication.getAuthorities().contains(sgaPartner)) {
			httpServletResponse.sendRedirect("partner");
		} else if (authentication.getAuthorities().contains(sgaUser)) {
			httpServletResponse.sendRedirect(".");
		}
		
	}

}
