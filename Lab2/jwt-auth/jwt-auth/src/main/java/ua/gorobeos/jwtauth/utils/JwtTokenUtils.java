package ua.gorobeos.jwtauth.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {

  @Value("${spring.jwt.secret}")
  private String secret;
  @Value("${spring.jwt.lifetime}")
  private Duration jwtLifetime;

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = getDefaultClaims(userDetails);
    return generateToken(userDetails, claims);
  }

  public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
    return Jwts
        .builder()
        .claims(extraClaims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtLifetime.toMillis()))
        .signWith(getSignInKey())
        .subject(userDetails.getUsername())
        .compact();
  }

  public String getUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims allClaims = extractAllClaims(token);
    return claimsResolver.apply(allClaims);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    return getUsername(token).equals(userDetails.getUsername());
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  private Map<String, Object> getDefaultClaims(UserDetails userDetails) {
    return Map.of("roles", userDetails.getAuthorities());
  }
}
