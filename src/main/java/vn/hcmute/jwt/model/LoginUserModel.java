package vn.hcmute.jwt.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record LoginUserModel(@NotBlank @Email String email, @NotBlank String password) { }
