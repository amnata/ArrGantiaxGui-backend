// package com.agriapp.controller;

// import com.agriapp.dto.UserProfileDTO;
// import com.agriapp.model.User;
// import com.agriapp.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;


// @RestController
// @RequestMapping("/api")
// public class UserController {

//     @Autowired
//     private UserRepository userRepository;

//     // Récupérer un user par ID
//     @GetMapping("/{id}")
//     public ResponseEntity<?> getUser(@PathVariable Long id) {
//         return userRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     // Utilisateur actuellement connecté
//     @GetMapping("/me")
//     public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
//         User user = userRepository.findByEmail(userDetails.getUsername())
//                 .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
//         return ResponseEntity.ok(user);
//     }

//     // ⚠️ LA ROUTE QUE TON ANGULAR ATTEND
//     // @GetMapping("/profil")
//     // public ResponseEntity<User> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
//     //     User user = userRepository.findByEmail(userDetails.getUsername())
//     //             .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
//     //     return ResponseEntity.ok(user);
//     // }

//     @GetMapping("/profil")
//     public ResponseEntity<User> getProfile() {
//         return ResponseEntity.ok(
//             userRepository.findById(1L).orElseThrow()
//         );
//     }



// }



// package com.agriapp.controller;

// import com.agriapp.model.User;
// import com.agriapp.repository.UserRepository;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.ResponseEntity;
// import org.springframework.beans.factory.annotation.Autowired;

// @RestController
// @RequestMapping("/api")
// public class UserController {

//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/profil")
//     public ResponseEntity<?> getProfile() {
//         return userRepository.findById(1L)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }


//      @GetMapping("/me")
//     public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
//         if (user == null) {
//             return ResponseEntity.status(401).body("Utilisateur non authentifié");
//         }

//         return ResponseEntity.ok(user);
//     }
// }

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.http.ResponseEntity;


// @RestController
// @RequestMapping("/api")
// public class UserController {

//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/profil")
//     public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {

//         if (userDetails == null) {
//             return ResponseEntity.status(403).body("Non authentifié");
//         }

//         User user = userRepository.findByEmail(userDetails.getUsername())
//                 .orElse(null);

//         if (user == null) {
//             return ResponseEntity.status(404).body("Utilisateur introuvable");
//         }

//         return ResponseEntity.ok(user);
//     }
// }
package com.agriapp.controller;

import com.agriapp.model.User;
import com.agriapp.repository.UserRepository;
import com.agriapp.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // Injection des dépendances via le constructeur
    @Autowired
    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Récupérer l'utilisateur connecté via le token JWT
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7); // retirer "Bearer "
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.status(401).body("Token invalide");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("Utilisateur introuvable");
        }

        return ResponseEntity.ok(user);
    }
}
