package vn.hcmute.jwt.service;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
@Service public class JwtService {
 private final byte[] secret; private final long expirationMs;
 public JwtService(@Value("${security.jwt.secret-key}") String key, @Value("${security.jwt.expiration-time}") long expiry) { if(key==null||key.isBlank()) throw new IllegalStateException("JWT secret is required"); try { secret=Base64.getDecoder().decode(key); } catch(IllegalArgumentException e) { throw new IllegalStateException("JWT secret must be Base64",e); } if(secret.length<32) throw new IllegalStateException("JWT secret must be at least 256 bits"); expirationMs=expiry; }
 public String generateToken(UserDetails user) { Instant now=Instant.now(); JWTClaimsSet claims=new JWTClaimsSet.Builder().subject(user.getUsername()).issueTime(Date.from(now)).expirationTime(Date.from(now.plusMillis(expirationMs))).build(); SignedJWT jwt=new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),claims); try { jwt.sign(new MACSigner(secret)); return jwt.serialize(); } catch(JOSEException e) { throw new JwtValidationException("Cannot sign JWT",e); } }
 public String extractUsername(String token) { return verifiedClaims(token).getSubject(); }
 public boolean isTokenValid(String token, UserDetails user) { return user.getUsername().equals(verifiedClaims(token).getSubject()); }
 public long getExpirationMs() { return expirationMs; }
 private JWTClaimsSet verifiedClaims(String token) { try { SignedJWT jwt=SignedJWT.parse(token); if(!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm())) throw new JwtValidationException("JWT algorithm is not accepted"); if(!jwt.verify(new MACVerifier(secret))) throw new JwtValidationException("JWT signature is invalid"); JWTClaimsSet c=jwt.getJWTClaimsSet(); Date now=new Date(); if(c.getSubject()==null||c.getSubject().isBlank()||c.getExpirationTime()==null||!c.getExpirationTime().after(now)) throw new JwtValidationException("JWT missing subject or expired"); if(c.getNotBeforeTime()!=null&&c.getNotBeforeTime().after(now)) throw new JwtValidationException("JWT is not active"); return c; } catch(ParseException|JOSEException e) { throw new JwtValidationException("JWT format is invalid",e); } }
}
