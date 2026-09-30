package vn.hcmute.jwt.service;
public class JwtValidationException extends RuntimeException { public JwtValidationException(String m) { super(m); } public JwtValidationException(String m, Throwable c) { super(m,c); } }
