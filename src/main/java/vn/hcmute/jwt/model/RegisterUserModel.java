package vn.hcmute.jwt.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record RegisterUserModel(@NotBlank @Email String email, @NotBlank @Size(min=6, max=100) String password, @NotBlank @Size(max=50) String fullName) { }
