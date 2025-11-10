package com.agriapp.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/detect") 
public class DetectionController {

    private final RestTemplate restTemplate;
    private static final String FLASK_BASE_URL = "http://localhost:5000";

    public DetectionController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Détection intelligente - CORRIGÉE
    @PostMapping("/disease")
    public ResponseEntity<?> detectDiseaseSmart(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("🧠 Détection maladie INTELLIGENTE - Fichier: " + file.getOriginalFilename());
            System.out.println("📊 Taille du fichier: " + file.getSize() + " bytes");

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le fichier est vide"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // ✅ CORRECTION: Utilisez /detect/smart au lieu de /detect/disease/smart
            String flaskUrl = FLASK_BASE_URL + "/detect/smart";
            System.out.println("🔄 Envoi vers Flask: " + flaskUrl);

            ResponseEntity<Map> response = restTemplate.exchange(
                    flaskUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            System.out.println("✅ Réponse reçue de Flask: " + response.getStatusCode());
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Erreur détection intelligente: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Échec de la détection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Détection plante - CORRIGÉE
    @PostMapping("/plant")
    public ResponseEntity<?> detectPlant(@RequestParam("file") MultipartFile file) { // ← CHANGE: "file" au lieu de "image"
        try {
            System.out.println("🌿 Détection plante - Fichier: " + file.getOriginalFilename());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le fichier est vide"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename())); // ← "image" pour Flask

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String flaskUrl = FLASK_BASE_URL + "/detect/plant";
            System.out.println("🔄 Envoi vers Flask: " + flaskUrl);

            ResponseEntity<Map> response = restTemplate.exchange(
                    flaskUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Erreur détection plante: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Échec de la détection de plante: " + e.getMessage()));
        }
    }

    // Santé - CORRIGÉE
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            String flaskUrl = FLASK_BASE_URL + "/health";
            System.out.println("🔍 Vérification santé Flask: " + flaskUrl);

            ResponseEntity<Map> response = restTemplate.getForEntity(flaskUrl, Map.class);
            System.out.println("✅ Flask API est en ligne");

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            System.err.println("❌ Flask API hors ligne: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "unhealthy");
            errorResponse.put("error", "Impossible de contacter le service Flask: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }

    // Classe interne pour gérer les fichiers multipart
    private static class MultipartInputStreamFileResource extends org.springframework.core.io.InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(java.io.InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() {
            return -1;
        }
    }
}