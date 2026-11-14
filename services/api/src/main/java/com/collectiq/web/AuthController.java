package com.collectiq.web;
import org.springframework.web.bind.annotation.*;import org.springframework.security.oauth2.jwt.*;import java.time.Instant;import java.util.*;
@RestController @RequestMapping("/api/auth") public class AuthController{
 private final JwtDecoder decoder;public AuthController(JwtDecoder d){decoder=d;}
 @GetMapping("/me")public Map<String,Object> me(@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt){return Map.of("sub",jwt.getSubject(),"email",jwt.getClaimAsString("email"),"name",Objects.toString(jwt.getClaimAsString("name"),"User"));}
 @PostMapping("/verify")public Map<String,Object> verify(@RequestBody Map<String,String> b){Jwt j=decoder.decode(b.get("token"));if(j.getExpiresAt()==null||j.getExpiresAt().isBefore(Instant.now()))throw new IllegalArgumentException("Expired token");return Map.of("valid",true,"email",Objects.toString(j.getClaimAsString("email"),""),"subject",j.getSubject());}
}