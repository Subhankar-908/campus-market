package com.ceog.campus_marketplace.WebSecurity;

import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JwtUntil {
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secratKey}")
    private String  secrateKey;
    private SecretKey getSecrateKey(){
        return Keys.hmacShaKeyFor(secrateKey.getBytes(StandardCharsets.UTF_8));
    }
    private final long EXPIRATION_TIME = 2*60*60*1000; // 15 minits

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId",user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecrateKey())
                .compact();
    }



    public Claims extractClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSecrateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token){
        return extractClaims(token).getSubject();
    }

    public boolean validToken(String token){
        try{
            extractClaims(token);
            return true;
        }
        catch(JwtException e){
            return false;
        }
    }

//    public boolean EditeValidToken(String AuthorizationHeader){
//        if(AuthorizationHeader!=null && AuthorizationHeader.startsWith("Bearer ")){
//            String token=AuthorizationHeader.substring(7);
//            if(validToken(token)){
//                return true;
//            }
//            else{
//                return false;
//            }
//        }
//        else{
//            return false;
//        }
//    }
}
