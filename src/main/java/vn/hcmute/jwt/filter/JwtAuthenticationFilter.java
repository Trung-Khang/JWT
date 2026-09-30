package vn.hcmute.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.hcmute.jwt.service.JwtService;

@Component public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final UserDetailsService users;
 public JwtAuthenticationFilter(JwtService jwt, UserDetailsService users) { this.jwt=jwt; this.users=users; }
 @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
   String header=request.getHeader(HttpHeaders.AUTHORIZATION);
   if (header == null || !header.startsWith("Bearer ")) { chain.doFilter(request,response); return; }
   try { String token=header.substring(7); String email=jwt.extractUsername(token); if (email != null && SecurityContextHolder.getContext().getAuthentication()==null) { UserDetails user=users.loadUserByUsername(email); if (jwt.isTokenValid(token,user)) { var auth=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities()); auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); SecurityContextHolder.getContext().setAuthentication(auth); } } chain.doFilter(request,response); }
   catch (RuntimeException ex) { SecurityContextHolder.clearContext(); response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); response.setContentType("application/json"); response.getWriter().write("{\"status\":401,\"error\":\"JWT không hợp lệ\"}"); }
 }
}
