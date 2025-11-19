package com.agriapp.controller;

import com.agriapp.dto.ChatMessageRequest;
import com.agriapp.dto.ChatMessageResponse;
import com.agriapp.dto.ProblemReportRequest;
import com.agriapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @RequestBody ChatMessageRequest request) {
        
        ChatMessageResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/voice-assistance")
    public ResponseEntity<ChatMessageResponse> requestVoiceAssistance() {
        ChatMessageResponse response = ChatMessageResponse.builder()
                .message("🎤 Assistant vocal en cours d'initialisation. Veuillez autoriser l'accès au microphone.")
                .timestamp(LocalDateTime.now())
                .type("info")
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/help")
    public ResponseEntity<ChatMessageResponse> getHelp() {
        String helpMessage = """
                📚 Aide agriapp Assistant:
                
                🌱 Détection de maladies:
                - Téléchargez une photo de votre plante
                - Recevez un diagnostic instantané
                - Obtenez des recommandations de traitement
                
                🌾 Classification des cultures:
                - Identifiez n'importe quelle plante
                - Consultez les probabilités détaillées
                
                📊 Suivi intelligent:
                - Suivez l'évolution de vos cultures
                - Visualisez des graphiques détaillés
                - Recevez des prédictions IA
                
                💬 Commandes disponibles:
                - "Comment traiter [maladie]?"
                - "Identifier cette plante"
                - "Conseils pour [culture]"
                - "État de mes cultures"
                
                Que puis-je faire pour vous ?
                """;
        
        ChatMessageResponse response = ChatMessageResponse.builder()
                .message(helpMessage)
                .timestamp(LocalDateTime.now())
                .type("text")
                .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/report-problem")
    public ResponseEntity<ChatMessageResponse> reportProblem(
            @RequestBody ProblemReportRequest request) {
        
        chatService.saveProblemReport(request);
        
        ChatMessageResponse response = ChatMessageResponse.builder()
                .message("✅ Votre signalement a été enregistré avec succès. " +
                        "Notre équipe support vous contactera dans les plus brefs délais. " +
                        "Numéro de ticket: #" + System.currentTimeMillis())
                .timestamp(LocalDateTime.now())
                .type("success")
                .build();
        
        return ResponseEntity.ok(response);
    }
}