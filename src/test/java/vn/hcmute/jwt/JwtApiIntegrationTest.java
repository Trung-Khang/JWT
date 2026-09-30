package vn.hcmute.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest @AutoConfigureMockMvc class JwtApiIntegrationTest {
 @Autowired MockMvc mvc; @Autowired ObjectMapper json;
 @Test void signupLoginAndProtectedEndpoints() throws Exception { String body="{\"email\":\"student@example.com\",\"password\":\"secret12\",\"fullName\":\"Nguyen Trung Khang\"}"; mvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andExpect(jsonPath("$.password").doesNotExist()); mvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict()); String response=mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"student@example.com\",\"password\":\"secret12\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.expiresIn").value(3600000)).andReturn().getResponse().getContentAsString(); String token=json.readTree(response).get("token").asText(); mvc.perform(get("/users/me").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("student@example.com")); mvc.perform(get("/users").header("Authorization","Bearer "+token)).andExpect(status().isOk()); }
 @Test void rejectsMissingMalformedAndInvalidLogin() throws Exception { mvc.perform(get("/users/me")).andExpect(status().isUnauthorized()); mvc.perform(get("/users/me").header("Authorization","Bearer bad.token.value")).andExpect(status().isUnauthorized()); mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"wrong@example.com\",\"password\":\"bad\"}")).andExpect(status().isUnauthorized()); mvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"not-an-email\"}")).andExpect(status().isBadRequest()); }
 @Test void rejectsTamperedExpiredNoneAndMissingClaimsTokens() throws Exception { String expired=signed(new JWTClaimsSet.Builder().subject("student@example.com").expirationTime(Date.from(Instant.now().minusSeconds(5))).build()); String noSubject=signed(new JWTClaimsSet.Builder().expirationTime(Date.from(Instant.now().plusSeconds(60))).build()); String none="eyJhbGciOiJub25lIn0.eyJzdWIiOiJzdHVkZW50QGV4YW1wbGUuY29tIiwiZXhwIjo0MTAyNDQ0ODAwfQ."; for(String token : new String[]{expired,noSubject,none,expired.substring(0,expired.length()-1)+"x"}) mvc.perform(get("/users/me").header("Authorization","Bearer "+token)).andExpect(status().isUnauthorized()); }
 private String signed(JWTClaimsSet claims) throws Exception { SignedJWT token=new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),claims); token.sign(new MACSigner(Base64.getDecoder().decode("MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="))); return token.serialize(); }
}
