package com.shopping.portal.util;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shopping.portal.service.EndUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Autowired
	private JWTUtil jwtUtil;
//
//	@Autowired
//	private UserDetailsService userDetailsService;

//    private final UserDetailsService userDetailsService;

//    public AuthTokenFilter(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    @Autowired
    EndUserDetailService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.debug("AuthTokenFilter called For URL : >>>>>> {}", request.getRequestURI());
		try {
			String jwt = jwtUtil.getJWTfromHeader(request);
			logger.debug("Authorization Header JWT Token :: {}", jwt);

			if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
				String userName = jwtUtil.getUsernameFromToken(jwt);
				UserDetails userDetails = userDetailService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken authenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
				logger.debug("Roles from JWT : {}", userDetails.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception e) {
			logger.error("Exception Occured in AuthTokenFilter :: Cannot set authentication :: {}", e);
		}
		filterChain.doFilter(request, response);
	}

}
