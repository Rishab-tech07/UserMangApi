package com.example.demo.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    //Helper method to get the signing Key
    private SecretKey getSignInkey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes,"HmacSHA256");  //algorithm for hashing
    }

    //Extract a single claim(payload) from the token
    public <T> T extractClaim(String token, Function<Claims,T> clamsResolver){
        final Claims claims= extractAllClaims(token);
        return clamsResolver.apply(claims);

    }

    //helper method to extract all the token
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInkey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //extracting username(subject) from the token
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }


    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        claims.put("roles",userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));
        return createToken(claims,userDetails.getUsername());
    }

    //create thr JWT token itself
    private String createToken(Map<String,Object>claims,String userName){
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(getSignInkey())
                .compact();
    }

    //validating the token
    public boolean validateToken(String token,UserDetails userDetails){
        try{
            //check if username matches and token is not expired
            return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }


    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
}