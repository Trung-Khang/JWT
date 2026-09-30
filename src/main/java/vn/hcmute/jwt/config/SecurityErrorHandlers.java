package vn.hcmute.jwt.config;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
@Component public class SecurityErrorHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {
 private void write(HttpServletResponse r, int status, String message) throws IOException { r.setStatus(status); r.setContentType(MediaType.APPLICATION_JSON_VALUE); r.getWriter().write("{\"status\":"+status+",\"error\":\""+message+"\"}"); }
 @Override public void commence(jakarta.servlet.http.HttpServletRequest q, HttpServletResponse r, AuthenticationException e) throws IOException { write(r,401,"Chưa xác thực hoặc token không hợp lệ"); }
 @Override public void handle(jakarta.servlet.http.HttpServletRequest q, HttpServletResponse r, AccessDeniedException e) throws IOException { write(r,403,"Không đủ quyền truy cập"); }
}
