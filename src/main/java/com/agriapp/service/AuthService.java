// package com.agriapp.service;

// import com.agriapp.dto.LoginRequest;
// import com.agriapp.dto.RegisterRequest;
// import com.agriapp.dto.ResetPasswordRequest;
// import com.agriapp.model.User;
// import com.agriapp.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;
// import java.util.UUID;

// @Service
// public class AuthService {

//     @Autowired
//     private UserRepository userRepository;

//     public Map<String, Object> register(RegisterRequest request) {
//         // Vérifier si l'email existe déjà
//         if (userRepository.existsByEmail(request.getEmail())) {
//             throw new IllegalArgumentException("Cet email est déjà utilisé");
//         }

//         // Créer le nouvel utilisateur
//         User user = new User(
//                 request.getEmail(),
//                 request.getPassword(), // TODO: Hasher le mot de passe avec BCrypt
//                 request.getNom(),
//                 request.getPrenom()
//         );

//         // Sauvegarder en base de données
//         user = userRepository.save(user);

//         // Préparer la réponse
//         Map<String, Object> response = new HashMap<>();
//         response.put("message", "Inscription réussie");
//         response.put("user", Map.of(
//                 "id", user.getId().toString(),
//                 "email", user.getEmail(),
//                 "nom", user.getNom(),
//                 "prenom", user.getPrenom()
//         ));

//         return response;
//     }

//     public Map<String, Object> login(LoginRequest request) {
//         // Rechercher l'utilisateur par email
//         Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

//         if (userOpt.isEmpty()) {
//             throw new IllegalArgumentException("Email ou mot de passe incorrect");
//         }

//         User user = userOpt.get();

//         // Vérifier le mot de passe (TODO: Utiliser BCrypt pour la comparaison)
//         if (!user.getPassword().equals(request.getPassword())) {
//             throw new IllegalArgumentException("Email ou mot de passe incorrect");
//         }

//         // Générer un token (simplifié - utilisez JWT en production)
//         String token = "jwt-token-" + UUID.randomUUID().toString();

//         // Préparer la réponse
//         Map<String, Object> response = new HashMap<>();
//         response.put("token", token);
//         response.put("user", Map.of(
//                 "id", user.getId().toString(),
//                 "email", user.getEmail(),
//                 "nom", user.getNom(),
//                 "prenom", user.getPrenom()
//         ));

//         return response;
//     }

//     public Map<String, Object> resetPassword(ResetPasswordRequest request) {
//         // Vérifier si l'utilisateur existe
//         Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

//         if (userOpt.isEmpty()) {
//             throw new IllegalArgumentException("Aucun compte trouvé avec cet email");
//         }

//         // TODO: Envoyer un vrai email avec un lien de réinitialisation

//         Map<String, Object> response = new HashMap<>();
//         response.put("message", "Email de réinitialisation envoyé");

//         return response;
//     }
// }

// package com.agriapp.service;

// import com.agriapp.dto.LoginRequest;
// import com.agriapp.dto.RegisterRequest;
// import com.agriapp.dto.ResetPasswordRequest;
// import com.agriapp.model.User;
// import com.agriapp.repository.UserRepository;
// import com.agriapp.security.JwtUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

// @Service
// public class AuthService {

//     private final UserRepository userRepository;
//     private final JwtUtil jwtUtil;
//     private final BCryptPasswordEncoder passwordEncoder;

//     @Autowired
//     public AuthService(UserRepository userRepository,
//                        JwtUtil jwtUtil,
//                        BCryptPasswordEncoder passwordEncoder) {
//         this.userRepository = userRepository;
//         this.jwtUtil = jwtUtil;
//         this.passwordEncoder = passwordEncoder;
//     }
//     // -------------------- REGISTER --------------------
//     public Map<String, Object> register(RegisterRequest request) {
//         if (userRepository.existsByEmail(request.getEmail())) {
//             throw new IllegalArgumentException("Cet email est déjà utilisé");
//         }
//         User user = new User(
//                 request.getEmail(),
//                 passwordEncoder.encode(request.getPassword()),
//                 request.getNom(),
//                 request.getPrenom()
//         );

//         user = userRepository.save(user);

//         Map<String, Object> response = new HashMap<>();
//         response.put("message", "Inscription réussie");
//         response.put("user", Map.of(
//                 "id", user.getId().toString(),
//                 "email", user.getEmail(),
//                 "nom", user.getNom(),
//                 "prenom", user.getPrenom()
//         ));

//         return response;
//     }

//     // -------------------- LOGIN --------------------
//     public Map<String, Object> login(LoginRequest request) {
//         Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

//         if (userOpt.isEmpty()) {
//             throw new IllegalArgumentException("Email ou mot de passe incorrect");
//         }

//         User user = userOpt.get();

//         // Vérifier le mot de passe hashé
//         if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//             throw new IllegalArgumentException("Email ou mot de passe incorrect");
//         }

//         // Générer un vrai JWT
//         String token = jwtUtil.generateToken(user.getEmail());
//         Map<String, Object> response = new HashMap<>();
//         response.put("token", token);
//         response.put("user", Map.of(
//                 "id", user.getId().toString(),
//                 "email", user.getEmail(),
//                 "nom", user.getNom(),
//                 "prenom", user.getPrenom()
//         ));

//         return response;
//     }
//     // -------------------- RESET PASSWORD --------------------
//     public Map<String, Object> resetPassword(ResetPasswordRequest request) {
//         Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

//         if (userOpt.isEmpty()) {
//             throw new IllegalArgumentException("Aucun compte trouvé avec cet email");
//         }
//         // TODO: envoyer un email réel avec un lien de réinitialisation

//         Map<String, Object> response = new HashMap<>();
//         response.put("message", "Email de réinitialisation envoyé");
//         return response;
//     }
// }

package com.agriapp.service;

import com.agriapp.dto.LoginRequest;
import com.agriapp.dto.RegisterRequest;
import com.agriapp.dto.ResetPasswordRequest;
import com.agriapp.model.User;
import com.agriapp.repository.UserRepository;
import com.agriapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

private final UserRepository userRepository;
private final JwtUtil jwtUtil;
private final BCryptPasswordEncoder passwordEncoder;

@Autowired
public AuthService(UserRepository userRepository,
                   JwtUtil jwtUtil,
                   BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
}

// -------------------- REGISTER --------------------
public Map<String, Object> register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
        throw new IllegalArgumentException("Cet email est déjà utilisé");
    }

    // Hasher le mot de passe avant de sauvegarder
    User user = new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getNom(),
            request.getPrenom()
    );

    user = userRepository.save(user);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Inscription réussie");
    response.put("user", Map.of(
            "id", user.getId().toString(),
            "email", user.getEmail(),
            "nom", user.getNom(),
            "prenom", user.getPrenom()
    ));

    return response;
}

// // -------------------- LOGIN --------------------
// public Map<String, Object> login(LoginRequest request) {
//     Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

//     if (userOpt.isEmpty()) {
//         throw new IllegalArgumentException("Email ou mot de passe incorrect");
//     }

//     User user = userOpt.get();

//     // Vérifier le mot de passe hashé
//     if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//         throw new IllegalArgumentException("Email ou mot de passe incorrect");
//     }

//     // Générer un JWT valide
//     String token = jwtUtil.generateToken(user.getEmail());

//     Map<String, Object> response = new HashMap<>();
//     response.put("token", token);
//     response.put("user", Map.of(
//             "id", user.getId().toString(),
//             "email", user.getEmail(),
//             "nom", user.getNom(),
//             "prenom", user.getPrenom()
//     ));

//     return response;
// }

// -------------------- LOGIN --------------------
public Map<String, Object> login(LoginRequest request) {
    Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
    if (userOpt.isEmpty()) {
        throw new IllegalArgumentException("Email ou mot de passe incorrect");
    }

    User user = userOpt.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new IllegalArgumentException("Email ou mot de passe incorrect");
    }

    String token = jwtUtil.generateToken(user.getEmail());

    Map<String, Object> response = new HashMap<>();
    response.put("token", token);
    
    // ✅ AJOUTER CES LIGNES
    response.put("userId", user.getId());  // ← IMPORTANT !
    response.put("email", user.getEmail());
    response.put("nom", user.getNom());
    response.put("prenom", user.getPrenom());
    
    // Garder aussi l'objet user pour compatibilité
    response.put("user", Map.of(
        "id", user.getId().toString(),
        "email", user.getEmail(),
        "nom", user.getNom(),
        "prenom", user.getPrenom()
    ));

    System.out.println("✅ Login réussi - User ID: " + user.getId());
    
    return response;
}

// -------------------- RESET PASSWORD --------------------
public Map<String, Object> resetPassword(ResetPasswordRequest request) {
    Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

    if (userOpt.isEmpty()) {
        throw new IllegalArgumentException("Aucun compte trouvé avec cet email");
    }

    // TODO: envoyer un email réel avec un lien de réinitialisation

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Email de réinitialisation envoyé");
    return response;
}

}
