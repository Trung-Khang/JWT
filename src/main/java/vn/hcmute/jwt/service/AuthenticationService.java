package vn.hcmute.jwt.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hcmute.jwt.entity.User;
import vn.hcmute.jwt.model.LoginUserModel;
import vn.hcmute.jwt.model.RegisterUserModel;
import vn.hcmute.jwt.repository.UserRepository;
@Service public class AuthenticationService {
 private final UserRepository users; private final PasswordEncoder encoder; private final AuthenticationManager manager;
 public AuthenticationService(UserRepository users, PasswordEncoder encoder, AuthenticationManager manager) { this.users=users; this.encoder=encoder; this.manager=manager; }
 public User signup(RegisterUserModel request) { if (users.findByEmail(request.email()).isPresent()) throw new IllegalStateException("Email đã được đăng ký"); return users.save(new User(request.email(), encoder.encode(request.password()), request.fullName())); }
 public User authenticate(LoginUserModel request) { manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())); return users.findByEmail(request.email()).orElseThrow(); }
}
