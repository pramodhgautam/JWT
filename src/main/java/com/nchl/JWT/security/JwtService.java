package com.nchl.JWT.security;

import com.nchl.JWT.model.CreditorUser;
import com.nchl.JWT.model.CreditorRole;
import com.nchl.JWT.model.CreditorUserRoleMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.one-time-expiration}")
    private long oneTimeTokenExpiration = TimeUnit.MINUTES.toMillis(5); // Default 5 minutes

    // Thread-safe token revocation store
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();
    private final long clockSkewSeconds = 30000000;

    public String generateToken(CreditorUser user) {
        Map<String, Object> claims = new HashMap<>();

        // Extract role names
        List<String> roles = user.getCreditorUserRoleMap().stream()
                .map(CreditorUserRoleMap::getCreditorRole)
                .map(CreditorRole::getName)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername()) // Using username as subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String username) {
        return buildToken(new HashMap<>(), username, jwtExpiration);
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // This will now store the username
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateOneTimeToken(CreditorUser userDetails) {
        Objects.requireNonNull(userDetails, "User details cannot be null");

        Map<String, Object> claims = new HashMap<>();
        claims.put("oneTime", true);
        claims.put("tokenType", "ONE_TIME");
        claims.put("iss", "your-issuer");
        claims.put("aud", "your-audience");
        claims.put("jti", UUID.randomUUID().toString()); // Unique token identifier

        // Type-safe role extraction from Set<CreditorUserRoleMap>
        Set<String> roles = Optional.ofNullable(userDetails.getCreditorUserRoleMap())
                .orElse(Collections.emptySet()) // Use emptySet instead of emptyList
                .stream()
                .filter(Objects::nonNull)
                .map(CreditorUserRoleMap::getCreditorRole)
                .filter(Objects::nonNull)
                .map(CreditorRole::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        claims.put("roles", new ArrayList<>(roles)); // Convert to List for JWT claims if needed

        return buildToken(claims, userDetails.getUsername(), oneTimeTokenExpiration);
    }

//    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final Claims claims = parseToken(token);

            // Check if token is revoked
            if (isTokenRevoked(token)) {
                log.warn("Token has been revoked: {}", token);
                return false;
            }

            // Check one-time token usage
            if (Boolean.TRUE.equals(claims.get("oneTime", Boolean.class))) {
                revokeToken(token); // Automatically revoke one-time tokens after first use
            }

            return claims.getSubject().equals(userDetails.getUsername());
        } catch (ExpiredJwtException ex) {
            log.warn("Token expired at {} (Current time: {})",
                    ex.getClaims().getExpiration(), new Date());
            return false;
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(clockSkewSeconds)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = parseToken(token);
        return (List<String>) claims.getOrDefault("roles", Collections.emptyList());
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }
}