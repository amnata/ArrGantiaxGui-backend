// package com.agriapp.controller;

// import com.agriapp.dto.UserProfileDTO;
// import com.agriapp.model.User;
// import com.agriapp.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api")
// @CrossOrigin(origins = "*")
// public class ProfilController {

//     @Autowired
//     private UserService userService;

//     /**
//      * Récupérer le profil de l'utilisateur connecté
//      */
//     @GetMapping("/profil")
//     public ResponseEntity<?> getUserProfile(Authentication authentication) {
//         try {
//             // Récupérer l'username depuis le token JWT
//             String username = authentication.getName();
            
//             // Récupérer l'utilisateur depuis la base de données
//             User user = userService.findByUsername(username);
            
//             if (user == null) {
//                 return ResponseEntity.notFound().build();
//             }
            
//             // Créer le DTO pour ne pas exposer le mot de passe
//             UserProfileDTO profileDTO = new UserProfileDTO(
//                 user.getId(),
//                 user.getUsername(),
//                 user.getEmail(),
//                 user.getFirstName(),
//                 user.getLastName(),
//                 user.getPhone(),
//                 user.getAddress(),
//                 user.getCreatedAt(),
//                 user.getRole()
//             );
            
//             return ResponseEntity.ok(profileDTO);
            
//         } catch (Exception e) {
//             return ResponseEntity.internalServerError()
//                 .body("Error retrieving profile: " + e.getMessage());
//         }
//     }

//     /**
//      * Mettre à jour le profil de l'utilisateur
//      */
//     @PutMapping("/profil")
//     public ResponseEntity<?> updateProfile(
//             @RequestBody UserProfileDTO profileDTO,
//             Authentication authentication) {
//         try {
//             String username = authentication.getName();
//             User updatedUser = userService.updateProfile(username, profileDTO);
            
//             UserProfileDTO responseDTO = new UserProfileDTO(
//                 updatedUser.getId(),
//                 updatedUser.getUsername(),
//                 updatedUser.getEmail(),
//                 updatedUser.getFirstName(),
//                 updatedUser.getLastName(),
//                 updatedUser.getPhone(),
//                 updatedUser.getAddress(),
//                 updatedUser.getCreatedAt(),
//                 updatedUser.getRole()
//             );
            
//             return ResponseEntity.ok(responseDTO);
            
//         } catch (Exception e) {
//             return ResponseEntity.badRequest()
//                 .body("Error updating profile: " + e.getMessage());
//         }
//     }
// }
