package vn.hcmute.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${security.jwt.secret-key}") String secretBase64, @Value("${security.jwt.expiration-time}") long expirationMs) {
        if (secretBase64 == null || secretBase64.isBlank()) throw new IllegalStateException("JWT_SECRET_BASE64 phải chứa khóa Base64 tối thiểu 256 bit");
        byte[] bytes;
        try { bytes = Base64.getDecoder().decode(secretBase64); } catch (IllegalArgumentException ex) { throw new IllegalStateException("JWT_SECRET_BASE64 không phải Base64 hợp lệ", ex); }
        if (bytes.length < 32) throw new IllegalStateException("JWT_SECRET_BASE64 phải giải mã thành ít nhất 32 byte");
        this.key = Keys.hmacShaKeyFor(bytes); this.expirationMs = expirationMs;
    }
    public String generateToken(UserDetails user) { Instant now=Instant.now(); return Jwts.builder().subject(user.getUsername()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expirationMs))).signWith(key).compact(); }
    public long getExpirationMs() { return expirationMs; }
    public String extractUsername(String token) { return claims(token).getSubject(); }
    public boolean isTokenValid(String token, UserDetails user) { Claims c=claims(token); return user.getUsername().equals(c.getSubject()) && c.getExpiration().after(new Date()); }
    private Claims claims(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
