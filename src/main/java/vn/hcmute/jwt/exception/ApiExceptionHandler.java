package vn.hcmute.jwt.exception;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> invalid(MethodArgumentNotValidException e) { return ResponseEntity.badRequest().body(Map.of("status",400,"error","Dữ liệu đầu vào không hợp lệ")); }
 @ExceptionHandler(IllegalStateException.class) ResponseEntity<Map<String,Object>> conflict(IllegalStateException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status",409,"error",e.getMessage())); }
 @ExceptionHandler(AuthenticationException.class) ResponseEntity<Map<String,Object>> unauthenticated(AuthenticationException e) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status",401,"error","Thông tin đăng nhập không hợp lệ")); }
 @ExceptionHandler(Exception.class) ResponseEntity<Map<String,Object>> unknown(Exception e) { return ResponseEntity.status(500).body(Map.of("status",500,"error","Lỗi máy chủ nội bộ")); }
}
