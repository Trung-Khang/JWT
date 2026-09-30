package vn.hcmute.jwt.controller;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.jwt.entity.User;
import vn.hcmute.jwt.model.UserResponse;
import vn.hcmute.jwt.service.UserService;
@RestController @RequestMapping("/users") public class UserController { private final UserService users; public UserController(UserService users) { this.users=users; } @GetMapping({"","/"}) public List<UserResponse> all() { return users.allUsers().stream().map(UserResponse::from).toList(); } @GetMapping("/me") public UserResponse me(@AuthenticationPrincipal User user) { return UserResponse.from(user); } }
