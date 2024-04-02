package com.demo.ClassBuddy.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Token is missing, invalid or expired.");
        errorResponse.put("httpStatus", "Unauthorized");
        errorResponse.put("timestamp", String.valueOf(Timestamp.valueOf(LocalDateTime.now())));
        errorResponse.put("redirectTo", "/login");

        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(writer, errorResponse);
        writer.flush();
    }
}
