package vn.hcmute.jwt.controller;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.jwt.entity.User;
import vn.hcmute.jwt.service.UserService;
@RestController @RequestMapping("/users") public class UserController { private final UserService users; public UserController(UserService users) { this.users=users; } @GetMapping({"","/"}) public List<User> all() { return users.allUsers(); } @GetMapping("/me") public User me(@AuthenticationPrincipal User user) { return user; } }
