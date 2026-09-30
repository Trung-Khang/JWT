package vn.hcmute.jwt.model;
import vn.hcmute.jwt.entity.User;
public record UserResponse(Long id, String fullName, String email, String images) { public static UserResponse from(User u) { return new UserResponse(u.getId(),u.getFullName(),u.getEmail(),u.getImages()); } }
