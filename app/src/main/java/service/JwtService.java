package service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // Implement JWT token generation and verification logic here

    public static final String SECRET = "ZW7SyxBKGi81hbB59iQ7EL6YSrrxlbyHGBoMUtHiqrk=";

    // anything to be extracted will give its token value
    // and return will extract the values from claims
    //by parsing usin  secret key
    public String GenerateJwtToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        // checks if the username and password in the JWT (after extracting it)
        // is present in the Db and is valid and  token is not expired

        // extract the data from JWT token
        final String username = extractUsername(token);

        // validate the data in the db
        return (username!= null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    private Boolean isTokenExpired(String token){
        return  extractExpiration(token).before(new Date());
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token){
        // parses the JWT by first checking if the key is correct
        // and then parse claims and return body as claims
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()  
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
 }
