package vn.hcmute.jwt.model;
public record LoginResponse(String token, long expiresIn) { }
