package vn.hcmute.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
