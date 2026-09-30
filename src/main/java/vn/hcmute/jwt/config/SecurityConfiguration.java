package vn.hcmute.jwt.config;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.hcmute.jwt.filter.JwtAuthenticationFilter;
@Configuration @EnableWebSecurity public class SecurityConfiguration {
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter filter) throws Exception { return http.csrf(csrf->csrf.disable()).cors(cors->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/auth/**","/login","/user/profile","/js/**","/css/**").permitAll().requestMatchers(HttpMethod.GET,"/users","/users/").authenticated().requestMatchers("/users/me").authenticated().anyRequest().denyAll()).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build(); }
 @Bean CorsConfigurationSource corsConfigurationSource(@org.springframework.beans.factory.annotation.Value("${app.cors.allowed-origin}") String origin) { var c=new CorsConfiguration(); c.setAllowedOrigins(List.of(origin)); c.setAllowedMethods(List.of("GET","POST","OPTIONS")); c.setAllowedHeaders(List.of("Authorization","Content-Type")); c.setAllowCredentials(true); var s=new UrlBasedCorsConfigurationSource(); s.registerCorsConfiguration("/**",c); return s; }
}
