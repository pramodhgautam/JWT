package com.nchl.JWT.security;

import com.nchl.JWT.model.CreditorUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Add a token revocation mechanism
    private final Set<String> revokedTokens = Collections.synchronizedSet(new HashSet<>());

    public String generateOneTimeToken(CreditorUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("oneTime", true); // Mark as one-time token

        // Extract roles from the CreditorUserRoleMap relationship
        Set<String> roles = userDetails.getCreditorUserRoleMap().stream()
                .map(roleMap -> roleMap.getCreditorRole().getName())
                .collect(Collectors.toSet());

        claims.put("roles", new ArrayList<>(roles)); // Convert to List if needed

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 min expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }

    public String generateToken(CreditorUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Extract roles from the CreditorUserRoleMap relationship
        Set<String> roles = userDetails.getCreditorUserRoleMap().stream()
                .map(roleMap -> roleMap.getCreditorRole().getName())
                .collect(Collectors.toSet());

        claims.put("roles", new ArrayList<>(roles)); // Convert to List if needed

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = (List<String>) claims.get("roles");
        return roles != null ? roles : Collections.emptyList(); // Return empty list instead of null
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()))
                && !isTokenRevoked(token)
                && !isTokenExpired(token); // Only check expiration if it exists
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }
}