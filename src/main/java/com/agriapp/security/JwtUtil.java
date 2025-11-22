// package com.agriapp.security;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.Claims;
// import org.springframework.stereotype.Component;

// import java.util.Date;

// @Component
// public class JwtUtil {

//     private final String SECRET_KEY = "NouveauItem253"; // à sécuriser plus tard

//     // Générer un JWT
//     public String generateToken(String email) {
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
//                 .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                 .compact();
//     }

//     // Extraire le username (email)
//     public String extractUsername(String token) {
//         Claims claims = Jwts.parser()
//                 .setSigningKey(SECRET_KEY)
//                 .parseClaimsJws(token)
//                 .getBody();
//         return claims.getSubject();
//     }

//     // Vérifier si le token est valide
//     public boolean validateToken(String token, String email) {
//         String username = extractUsername(token);
//         return username.equals(email) && !isTokenExpired(token);
//     }

//     private boolean isTokenExpired(String token) {
//         Claims claims = Jwts.parser()
//                 .setSigningKey(SECRET_KEY)
//                 .parseClaimsJws(token)
//                 .getBody();
//         return claims.getExpiration().before(new Date());
//     }
// }


package com.agriapp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {


// Clé secrète pour signer le JWT (32 caractères minimum)
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
// -------------------- GENERER UN TOKEN --------------------
public String generateToken(String email) {
    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}

// -------------------- EXTRAIRE LE USERNAME --------------------
public String extractUsername(String token) {
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    return claims.getSubject();
}

// -------------------- VALIDER LE TOKEN --------------------
public boolean validateToken(String token, String email) {
    String username = extractUsername(token);
    return username.equals(email) && !isTokenExpired(token);
}

// -------------------- VERIFIER EXPIRATION --------------------
private boolean isTokenExpired(String token) {
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    return claims.getExpiration().before(new Date());
}

}
