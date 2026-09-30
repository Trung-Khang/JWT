package vn.hcmute.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity @Table(name = "users")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 50) private String fullName;
    @Column(nullable = false, unique = true, length = 100) private String email;
    @Column(nullable = false, length = 500) private String images = "";
    @JsonIgnore @Column(nullable = false) private String password;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    protected User() { }
    public User(String email, String password, String fullName) { this.email=email; this.password=password; this.fullName=fullName; }
    @PrePersist void created() { createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate void updated() { updatedAt=Instant.now(); }
    public Long getId() { return id; } public String getFullName() { return fullName; } public String getEmail() { return email; }
    public String getImages() { return images; } public void setImages(String images) { this.images = images == null ? "" : images; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
