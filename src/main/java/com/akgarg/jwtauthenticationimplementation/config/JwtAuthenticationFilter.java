package com.akgarg.jwtauthenticationimplementation.config;

import com.akgarg.jwtauthenticationimplementation.helper.JWTHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// this filer checks if incoming request is valid or not (in terms of JWT token)
// if JWT token is available then user undergoes validation otherwise normal request is forwarded to DispatcherServlet
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTHelperUtil jwtHelperUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken;
        String username = "";

        if (requestTokenHeader != null && !requestTokenHeader.equals("") && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = this.jwtHelperUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            if (!username.equals("") && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}