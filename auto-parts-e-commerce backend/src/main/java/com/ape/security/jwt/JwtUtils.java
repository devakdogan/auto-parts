package com.ape.security.jwt;

import com.gpm.exception.message.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${management.autoparts.app.jwtSecret}")
    // jwtSecret değerini application.yml dosyasından alıyor
    private String jwtSecret ;


    @Value("${management.autoparts.app.jwtExpirationMS}") // jwtExpirationMs değerini application.yml dosyasından alıyor
    private long jwtExpirationMs;

    private Key getSigningKey() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return key;
    }

    // JWT token üretecek
    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).
                claim("role",userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                setIssuedAt(new Date()).
                setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).
                signWith(getSigningKey()).
                compact();
    }


    // JWT token içinden kullanıcının email bilgisi alınacak
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();

    }

    // JWT token valide edecek
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | IllegalArgumentException e ) {
            logger.error(ErrorMessage.JWTTOKEN_ERROR_MESSAGE);
        }
        return false ;
    }
}
