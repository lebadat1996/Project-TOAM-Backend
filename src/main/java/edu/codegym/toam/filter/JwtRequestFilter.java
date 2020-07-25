package edu.codegym.toam.filter;

import edu.codegym.toam.service.account.AccountDetailService;
import edu.codegym.toam.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountDetailService accountDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");

        String jwt = null;
        String username = null;
        String roleName = null;
        if (authorization != null) {
            jwt = authorization.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (username != null && authentication == null) {
            UserDetails userDetails = accountDetailService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authen
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authen.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authen);
            }
        }
        filterChain.doFilter(request, response);
    }
}
