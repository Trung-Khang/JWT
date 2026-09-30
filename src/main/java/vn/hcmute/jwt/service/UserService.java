package vn.hcmute.jwt.service;
import java.util.List;
import org.springframework.stereotype.Service;
import vn.hcmute.jwt.entity.User;
import vn.hcmute.jwt.repository.UserRepository;
@Service public class UserService { private final UserRepository users; public UserService(UserRepository users) { this.users=users; } public List<User> allUsers() { return users.findAll(); } }
