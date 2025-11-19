package com.agriapp.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class DialogflowService {

    @Value("${dialogflow.project-id}")
    private String projectId;

    @Value("${dialogflow.language-code:fr}")
    private String languageCode;

    @Value("${dialogflow.credentials-path}")
    private String credentialsPath;

    private SessionsClient sessionsClient;

    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsPath));

            SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            sessionsClient = SessionsClient.create(sessionsSettings);
            log.info("Dialogflow initialized successfully for project: {}", projectId);
        } catch (IOException e) {
            log.error("Failed to initialize Dialogflow: {}", e.getMessage());
            throw new RuntimeException("Dialogflow initialization failed", e);
        }
    }

    /**
     * Envoie un message texte à Dialogflow et retourne la réponse
     */
    public String detectIntentText(String text, String sessionId) {
        try {
            // Créer le nom de la session
            SessionName session = SessionName.of(projectId, sessionId);

            // Créer l'input texte
            TextInput.Builder textInput = TextInput.newBuilder()
                    .setText(text)
                    .setLanguageCode(languageCode);

            // Créer l'input de la requête
            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

            // Détecter l'intention
            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            QueryResult queryResult = response.getQueryResult();

            log.info("Intent detected: {} (confidence: {})", 
                    queryResult.getIntent().getDisplayName(),
                    queryResult.getIntentDetectionConfidence());

            // Retourner le texte de réponse
            return queryResult.getFulfillmentText();

        } catch (Exception e) {
            log.error("Error detecting intent: {}", e.getMessage());
            return "Désolé, je n'ai pas pu traiter votre demande. Veuillez réessayer.";
        }
    }

    /**
     * Génère un ID de session unique pour chaque conversation
     */
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Ferme les ressources Dialogflow
     */
    public void shutdown() {
        if (sessionsClient != null) {
            sessionsClient.close();
        }
    }
}