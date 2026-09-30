package vn.hcmute.jwt.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.hcmute.jwt.repository.UserRepository;
@Configuration public class ApplicationConfiguration {
 @Bean UserDetailsService userDetailsService(UserRepository users) { return email -> users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng")); }
 @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 @Bean DaoAuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder pe) { var p=new DaoAuthenticationProvider(pe); p.setUserDetailsService(uds); return p; }
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception { return c.getAuthenticationManager(); }
}
