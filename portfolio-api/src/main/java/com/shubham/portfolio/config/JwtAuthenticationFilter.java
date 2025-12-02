package com.shubham.portfolio.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shubham.portfolio.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author SinghShubham
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	private static final List<String> PUBLIC_PATHS = Arrays.asList("/api/v1/auth/**", "/api/v1/projects",
			"/api/v1/projects/**", "/api/v1/profile-data", "/api/v1/contact", "/ping", "/actuator/**", "/error");

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
			filterChain.doFilter(request, response);
			return;
		}

		String requestPath = request.getServletPath();

		boolean isPublicPath = PUBLIC_PATHS.stream().anyMatch(path -> pathMatcher.match(path, requestPath));

		if (isPublicPath
				&& (pathMatcher.match("/ping", requestPath) || pathMatcher.match("/actuator/**", requestPath))) {
			filterChain.doFilter(request, response);
			return;
		}

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			jwt = authHeader.substring(7);
			username = jwtService.extractUsername(jwt);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (Exception e) {
			logger.error("Could not set user authentication in security context", e);
		}

		filterChain.doFilter(request, response);
	}
}
