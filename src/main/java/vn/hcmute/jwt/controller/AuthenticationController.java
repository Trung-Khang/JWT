package vn.hcmute.jwt.controller;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.jwt.entity.User;
import vn.hcmute.jwt.model.*;
import vn.hcmute.jwt.service.AuthenticationService;
import vn.hcmute.jwt.service.JwtService;
@RestController @RequestMapping("/auth") public class AuthenticationController {
 private final AuthenticationService auth; private final JwtService jwt; public AuthenticationController(AuthenticationService auth, JwtService jwt) { this.auth=auth; this.jwt=jwt; }
 @PostMapping("/signup") @ResponseStatus(HttpStatus.CREATED) public User signup(@Valid @RequestBody RegisterUserModel r) { return auth.signup(r); }
 @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginUserModel r) { User u=auth.authenticate(r); return new LoginResponse(jwt.generateToken(u), jwt.getExpirationMs()); }
}
