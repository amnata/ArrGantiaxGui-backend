package com.agriapp.service;

import com.agriapp.dto.LoginRequest;
import com.agriapp.dto.RegisterRequest;
import com.agriapp.dto.ResetPasswordRequest;
import com.agriapp.model.User;
import com.agriapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // Créer le nouvel utilisateur
        User user = new User(
                request.getEmail(),
                request.getPassword(), // TODO: Hasher le mot de passe avec BCrypt
                request.getNom(),
                request.getPrenom()
        );

        // Sauvegarder en base de données
        user = userRepository.save(user);

        // Préparer la réponse
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

    public Map<String, Object> login(LoginRequest request) {
        // Rechercher l'utilisateur par email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        User user = userOpt.get();

        // Vérifier le mot de passe (TODO: Utiliser BCrypt pour la comparaison)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        // Générer un token (simplifié - utilisez JWT en production)
        String token = "jwt-token-" + UUID.randomUUID().toString();

        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
                "id", user.getId().toString(),
                "email", user.getEmail(),
                "nom", user.getNom(),
                "prenom", user.getPrenom()
        ));

        return response;
    }

    public Map<String, Object> resetPassword(ResetPasswordRequest request) {
        // Vérifier si l'utilisateur existe
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Aucun compte trouvé avec cet email");
        }

        // TODO: Envoyer un vrai email avec un lien de réinitialisation

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Email de réinitialisation envoyé");

        return response;
    }
}