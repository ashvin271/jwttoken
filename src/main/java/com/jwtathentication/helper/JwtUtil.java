package com.jwtathentication.helper;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jwtathentication.JwtathenticationApplication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

//generated token 
//validate
//isExpair
//get code from google jwtuttil class code



@Component
public class JwtUtil {
	

	private String SECRET_KEY = "ashvin789";
	private long EXPIRE_TOKEN = 1800000l;
	private long EXPIRE_REFERSH_TOKEN = 3600000l;

   
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
         claims = Jwts.claims().setSubject(userDetails.getUsername());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TOKEN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    
    public String generateRefreshToken(UserDetails userDetails) {

		Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
		claims.put("scopes", Arrays.asList("ROLE_REFRESH_TOKEN"));

		Date expiryDate = new Date(System.currentTimeMillis() + EXPIRE_REFERSH_TOKEN);

		return Jwts.builder().setClaims(claims).setIssuer("gramercyortho").setId(UUID.randomUUID().toString())
				.setIssuedAt(new Date()).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}
    
    /**
	 * This method returns the subject({@link User} email1) from claims
	 * 
	 * @param token JWT token
	 * @return {@link User} email1
	 */
	public String getUserFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}
	
	/**
	 * This method checks if JWT token is expired or not
	 * 
	 * @param authToken JWT token
	 * @return {@link Boolean}
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			JwtathenticationApplication.LOGGER.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			JwtathenticationApplication.LOGGER.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			JwtathenticationApplication.LOGGER.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			JwtathenticationApplication.LOGGER.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			JwtathenticationApplication.LOGGER.error("JWT claims string is empty.");
		}
		return false;
	}
}
