// // package com.agriapp.service;

// // import com.agriapp.dto.ChatMessageRequest;
// // import com.agriapp.dto.ChatMessageResponse;
// // import com.agriapp.dto.ProblemReportRequest;
// // import com.agriapp.entity.ChatMessage;
// // import com.agriapp.entity.ProblemReport;
// // import com.agriapp.repository.ChatMessageRepository;
// // import com.agriapp.repository.ProblemReportRepository;
// // import lombok.RequiredArgsConstructor;
// // import org.springframework.stereotype.Service;

// // import java.time.LocalDateTime;
// // import java.util.HashMap;
// // import java.util.Map;

// // @Service
// // @RequiredArgsConstructor
// // public class ChatService {

// //     private final ChatMessageRepository chatMessageRepository;
// //     private final ProblemReportRepository problemReportRepository;

// //     // Mots-clés pour détecter l'intention de l'utilisateur
// //     private static final Map<String, String> INTENT_RESPONSES = new HashMap<>();

// // static {
// //     INTENT_RESPONSES.put("maladie", 
// //         "🔍 Pour détecter une maladie, rendez-vous dans la section 'Détection' " +
// //         "et téléchargez une photo claire de votre plante. Notre IA analysera l'image " +
// //         "et vous fournira un diagnostic précis avec des recommandations de traitement.");

// //     INTENT_RESPONSES.put("identifier", 
// //         "🌱 Pour identifier une plante, allez dans la section 'Classification' " +
// //         "et prenez une photo de la plante. L'IA vous indiquera l'espèce avec les " +
// //         "probabilités détaillées.");

// //     INTENT_RESPONSES.put("suivi", 
// //         "📊 Dans la section 'Suivi', vous pouvez enregistrer les données de vos cultures : " +
// //         "hauteur, stade de croissance, état sanitaire, etc. Visualisez l'évolution en " +
// //         "graphiques et obtenez des prédictions intelligentes.");

// //     INTENT_RESPONSES.put("arrosage", 
// //         "💧 Conseils d'arrosage :\n" +
// //         "- Arrosez tôt le matin ou en soirée\n" +
// //         "- Évitez de mouiller les feuilles\n" +
// //         "- Adaptez la fréquence selon la météo\n" +
// //         "- Utilisez nos capteurs d'humidité pour un suivi optimal");

// //     INTENT_RESPONSES.put("engrais", 
// //         "🌿 Conseils sur les engrais :\n" +
// //         "- Utilisez des engrais adaptés à chaque type de culture\n" +
// //         "- Respectez les doses recommandées\n" +
// //         "- Appliquez selon le stade de croissance\n" +
// //         "- Consultez notre section recommandations pour plus de détails");

// //     // Nouveaux intents
// //     INTENT_RESPONSES.put("récolte", 
// //         "🌾 Pour la récolte :\n" +
// //         "- Vérifiez la maturité de votre culture\n" +
// //         "- Récoltez tôt le matin pour une meilleure conservation\n" +
// //         "- Suivez nos conseils spécifiques selon le type de plante");

// //     INTENT_RESPONSES.put("plante malade", 
// //         "⚠️ Si votre plante présente des signes de maladie :\n" +
// //         "- Prenez une photo claire de la feuille ou du fruit\n" +
// //         "- Utilisez la section 'Détection' pour analyser\n" +
// //         "- Suivez les recommandations de traitement");

// //     INTENT_RESPONSES.put("fertilisation", 
// //         "🌱 Fertilisation optimale :\n" +
// //         "- Équilibrez l'azote, le phosphore et le potassium\n" +
// //         "- Fertilisez selon le stade de croissance\n" +
// //         "- Utilisez nos recommandations personnalisées pour chaque culture");

// //     INTENT_RESPONSES.put("conseils", 
// //         "💡 Je peux vous donner des conseils sur :\n" +
// //         "- Arrosage et irrigation\n" +
// //         "- Engrais et fertilisation\n" +
// //         "- Protection contre les maladies\n" +
// //         "- Optimisation du suivi des cultures");

// //     INTENT_RESPONSES.put("météo", 
// //         "☀️ Pour la météo :\n" +
// //         "- Vérifiez les prévisions dans votre région\n" +
// //         "- Adaptez l'arrosage et la protection des cultures\n" +
// //         "- Nos capteurs peuvent également vous aider à suivre l'humidité du sol");

// //     INTENT_RESPONSES.put("capteurs", 
// //         "📡 Nos capteurs permettent :\n" +
// //         "- Suivi de l'humidité du sol\n" +
// //         "- Mesure de la luminosité\n" +
// //         "- Suivi de la température\n" +
// //         "- Aide à la décision pour arrosage et fertilisation");

// //         // Salut / Bonjour
// //     INTENT_RESPONSES.put("bonjour", 
// //         "👋 Bonjour ! Je suis votre assistant AgriApp. " +
// //         "Je peux vous aider à détecter des maladies, identifier des plantes, suivre vos cultures et plus encore. " +
// //         "Comment puis-je vous aider aujourd'hui ?");

// //     INTENT_RESPONSES.put("salut", 
// //         "👋 Salut ! Ravi de vous revoir. " +
// //         "Vous pouvez me poser des questions sur vos plantations, ou demander des conseils pour vos cultures.");

// //     // Remerciements
// //     INTENT_RESPONSES.put("merci", 
// //         "😊 Avec plaisir ! N'hésitez pas si vous avez d'autres questions. " +
// //         "Je suis là pour vous aider !");

// //     INTENT_RESPONSES.put("merci beaucoup", 
// //         "🙏 Je vous en prie ! Votre succès en agriculture est ma priorité.");

// //     // Formules de politesse / au revoir
// //     INTENT_RESPONSES.put("au revoir", 
// //         "👋 Au revoir ! Bonne continuation pour vos cultures. " +
// //         "Revenez me voir quand vous voulez.");

// //     INTENT_RESPONSES.put("bonne journée", 
// //         "🌞 Merci ! Passez une excellente journée et prenez soin de vos plantes.");

// //     INTENT_RESPONSES.put("merci beaucoup", 
// //         "🙏 Je vous en prie ! Votre succès en agriculture est ma priorité.");
// //     INTENT_RESPONSES.put("merci bien", 
// //         "😄 C'est un plaisir ! Je suis là pour vous accompagner dans vos cultures.");
// //     INTENT_RESPONSES.put("merci infiniment", 
// //         "🌟 Avec plaisir ! N'hésitez pas à revenir pour plus de conseils.");
// //     INTENT_RESPONSES.put("maladie_oignon_alternaria",
// //     "⚠️ Alternaria sur l’oignon provoque des taches brunes/noires concentriques sur les feuilles, favorisées par l’humidité. Rotation des cultures et fongicides adaptés sont recommandés.");

// //     INTENT_RESPONSES.put("maladie_oignon_downy",
// //         "💧 Mildiou de l’oignon : taches jaunes sur les feuilles avec duvet gris à l’arrière. Humidité élevée favorise la maladie. Utilisez variétés résistantes et fongicides.");

// //     INTENT_RESPONSES.put("maladie_oignon_botrytis",
// //         "🌿 Pourriture grise (Botrytis) : taches grises sur feuilles et bulbes. Favorisée par temps humide. Enlevez les parties infectées et appliquez fongicides.");
// //     INTENT_RESPONSES.put("maladie_arachide_tache_folliaire",
// //     "🌱 Tache foliaire : petites taches sombres sur les feuilles, pouvant provoquer leur chute et réduire le rendement. Rotation des cultures et fongicides recommandés.");

// //     INTENT_RESPONSES.put("maladie_arachide_rouille",
// //         "🟠 Rouille : pustules rouges sur la face inférieure des feuilles. Réduit la croissance et le rendement. Utiliser variétés résistantes et traitements fongicides.");

// //     INTENT_RESPONSES.put("maladie_arachide_mildiou",
// //         "💧 Mildiou de l’arachide : taches jaunes puis brunes sur feuilles, surtout par temps humide. Prévention : semences saines et fongicides adaptés.");

// //     INTENT_RESPONSES.put("maladie_riz_blast",
// //     "🔥 Blast du riz : taches brun-gris sur feuilles et talles, souvent en forme d’œil. Réduit le rendement. Utiliser variétés résistantes et bonnes pratiques culturales.");

// //     INTENT_RESPONSES.put("maladie_riz_tache_brun",
// //         "⚫ Tache brune : petites taches brunes sur les feuilles, surtout jeunes plants. Fertilisation équilibrée et semences saines recommandées.");

// //     INTENT_RESPONSES.put("maladie_riz_mildiou",
// //         "💧 Mildiou du riz : taches chlorotiques sur feuilles puis grisâtres avec duvet. Favorisé par humidité élevée. Prévention : semences saines et traitement fongicide.");


// // }

// //     public ChatMessageResponse processMessage(ChatMessageRequest request) {
// //         // Sauvegarder le message de l'utilisateur
// //         ChatMessage userMessage = ChatMessage.builder()
// //                 .message(request.getMessage())
// //                 .isUserMessage(true)
// //                 .timestamp(LocalDateTime.now())
// //                 .build();
        
// //         chatMessageRepository.save(userMessage);

// //         // Analyser l'intention et générer une réponse
// //         String response = generateResponse(request.getMessage().toLowerCase());

// //         // Sauvegarder la réponse du bot
// //         ChatMessage botMessage = ChatMessage.builder()
// //                 .message(response)
// //                 .isUserMessage(false)
// //                 .timestamp(LocalDateTime.now())
// //                 .build();
        
// //         chatMessageRepository.save(botMessage);

// //         return ChatMessageResponse.builder()
// //                 .message(response)
// //                 .timestamp(LocalDateTime.now())
// //                 .type("text")
// //                 .build();
// //     }

// //     private String generateResponse(String message) {
// //         // Recherche de mots-clés pour déterminer l'intention
// //         for (Map.Entry<String, String> entry : INTENT_RESPONSES.entrySet()) {
// //             if (message.contains(entry.getKey())) {
// //                 return entry.getValue();
// //             }
// //         }

// //         // Réponses spécifiques pour des questions courantes
// //         if (message.contains("bonjour") || message.contains("salut") || message.contains("hello")) {
// //             return "👋 Bonjour ! Je suis votre assistant agriapp. " +
// //                    "Je peux vous aider avec la détection de maladies, " +
// //                    "l'identification de plantes, le suivi de vos cultures et bien plus. " +
// //                    "Comment puis-je vous aider aujourd'hui ?";
// //         }

// //         if (message.contains("merci")) {
// //             return "😊 Avec plaisir ! N'hésitez pas si vous avez d'autres questions. " +
// //                    "Je suis là pour vous aider !";
// //         }

// //         if (message.contains("aide") || message.contains("help")) {
// //             return "📚 Je peux vous aider avec :\n" +
// //                    "🔍 La détection de maladies des plantes\n" +
// //                    "🌱 L'identification de cultures\n" +
// //                    "📊 Le suivi intelligent de vos plantations\n" +
// //                    "💡 Des conseils personnalisés\n\n" +
// //                    "Posez-moi vos questions ou utilisez les suggestions ci-dessus !";
// //         }

// //         // Réponse par défaut avec IA simulée
// //         return "🤔 Je comprends votre question. Pour une réponse précise, " +
// //                "pourriez-vous préciser si vous souhaitez des informations sur :\n" +
// //                "• La détection de maladies 🔍\n" +
// //                "• L'identification de plantes 🌱\n" +
// //                "• Le suivi de vos cultures 📊\n" +
// //                "• Des conseils d'entretien 💡";
// //     }

// //     public void saveProblemReport(ProblemReportRequest request) {
// //         ProblemReport report = ProblemReport.builder()
// //                 .description(request.getDescription())
// //                 .timestamp(LocalDateTime.now())
// //                 .status("PENDING")
// //                 .build();
        
// //         problemReportRepository.save(report);
// //     }
// // }

// package com.agriapp.service;

// import com.agriapp.dto.ChatMessageRequest;
// import com.agriapp.dto.ChatMessageResponse;
// import com.agriapp.dto.ProblemReportRequest;
// import com.agriapp.entity.ChatMessage;
// import com.agriapp.entity.ProblemReport;
// import com.agriapp.repository.ChatMessageRepository;
// import com.agriapp.repository.ProblemReportRepository;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class ChatService {

//     private final ChatMessageRepository chatMessageRepository;
//     private final ProblemReportRepository problemReportRepository;

//     // Structure pour stocker les intents avec leurs mots-clés et réponses
//     private static final Map<String, IntentData> INTENTS = new HashMap<>();

//     static class IntentData {
//         String response;
//         List<String> keywords;
//         int priority; // Pour gérer les priorités en cas de multiples correspondances

//         IntentData(String response, int priority, String... keywords) {
//             this.response = response;
//             this.keywords = Arrays.asList(keywords);
//             this.priority = priority;
//         }
//     }

//     static {
//         // Salutations (priorité haute)
//         INTENTS.put("greeting", new IntentData(
//             "👋 Bonjour ! Je suis votre assistant AgriApp. " +
//             "Je peux vous aider avec la détection de maladies, " +
//             "l'identification de plantes, le suivi de vos cultures et bien plus. " +
//             "Comment puis-je vous aider aujourd'hui ?",
//             10,
//             "bonjour", "salut", "hello", "hi", "bonsoir", "hey"
//         ));

//         // Remerciements
//         INTENTS.put("thanks", new IntentData(
//             "😊 Avec plaisir ! N'hésitez pas si vous avez d'autres questions. " +
//             "Je suis là pour vous aider !",
//             10,
//             "merci", "thanks", "merci beaucoup", "merci bien", "merci infiniment"
//         ));

//         // Au revoir
//         INTENTS.put("goodbye", new IntentData(
//             "👋 Au revoir ! Bonne continuation pour vos cultures. " +
//             "Revenez me voir quand vous voulez.",
//             10,
//             "au revoir", "bye", "à bientôt", "à plus", "tchao", "ciao", "bonne journée"
//         ));

//         // Aide générale
//         INTENTS.put("help", new IntentData(
//             "📚 Je peux vous aider avec :\n" +
//             "🔍 La détection de maladies des plantes\n" +
//             "🌱 L'identification de cultures\n" +
//             "📊 Le suivi intelligent de vos plantations\n" +
//             "💡 Des conseils personnalisés\n\n" +
//             "Posez-moi vos questions ou utilisez les suggestions ci-dessus !",
//             9,
//             "aide", "help", "assistance", "comment", "guide"
//         ));

//         // Détection de maladies
//         INTENTS.put("disease_detection", new IntentData(
//             "🔍 Pour détecter une maladie, rendez-vous dans la section 'Détection' " +
//             "et téléchargez une photo claire de votre plante. Notre IA analysera l'image " +
//             "et vous fournira un diagnostic précis avec des recommandations de traitement.",
//             8,
//             "maladie", "malade", "disease", "détecter maladie", "diagnostic", "symptômes", "problème plante"
//         ));

//         // Identification de plantes
//         INTENTS.put("plant_identification", new IntentData(
//             "🌱 Pour identifier une plante, allez dans la section 'Classification' " +
//             "et prenez une photo de la plante. L'IA vous indiquera l'espèce avec les " +
//             "probabilités détaillées.",
//             8,
//             "identifier", "identification", "quelle plante", "espèce", "classification", "reconnaître plante"
//         ));

//         // Suivi des cultures
//         INTENTS.put("crop_monitoring", new IntentData(
//             "📊 Dans la section 'Suivi', vous pouvez enregistrer les données de vos cultures : " +
//             "hauteur, stade de croissance, état sanitaire, etc. Visualisez l'évolution en " +
//             "graphiques et obtenez des prédictions intelligentes.",
//             7,
//             "suivi", "monitoring", "suivre culture", "évolution", "graphique", "statistiques"
//         ));

//         // Arrosage
//         INTENTS.put("watering", new IntentData(
//             "💧 Conseils d'arrosage :\n" +
//             "- Arrosez tôt le matin ou en soirée\n" +
//             "- Évitez de mouiller les feuilles\n" +
//             "- Adaptez la fréquence selon la météo\n" +
//             "- Utilisez nos capteurs d'humidité pour un suivi optimal",
//             7,
//             "arrosage", "arroser", "irrigation", "eau", "irriguer", "humidité"
//         ));

//         // Engrais / Fertilisation
//         INTENTS.put("fertilization", new IntentData(
//             "🌿 Conseils sur les engrais :\n" +
//             "- Utilisez des engrais adaptés à chaque type de culture\n" +
//             "- Respectez les doses recommandées\n" +
//             "- Appliquez selon le stade de croissance\n" +
//             "- Consultez notre section recommandations pour plus de détails\n" +
//             "💡 Fertilisation optimale : Équilibrez l'azote, le phosphore et le potassium",
//             7,
//             "engrais", "fertilisation", "fertiliser", "nutriment", "azote", "phosphore", "potassium", "npk"
//         ));

//         // Récolte
//         INTENTS.put("harvest", new IntentData(
//             "🌾 Pour la récolte :\n" +
//             "- Vérifiez la maturité de votre culture\n" +
//             "- Récoltez tôt le matin pour une meilleure conservation\n" +
//             "- Suivez nos conseils spécifiques selon le type de plante",
//             7,
//             "récolte", "récolter", "harvest", "maturité", "quand récolter"
//         ));

//         // Conseils généraux
//         INTENTS.put("general_advice", new IntentData(
//             "💡 Je peux vous donner des conseils sur :\n" +
//             "- Arrosage et irrigation\n" +
//             "- Engrais et fertilisation\n" +
//             "- Protection contre les maladies\n" +
//             "- Optimisation du suivi des cultures",
//             6,
//             "conseil", "recommandation", "astuce", "suggestion", "comment faire"
//         ));

//         // Météo
//         INTENTS.put("weather", new IntentData(
//             "☀️ Pour la météo :\n" +
//             "- Vérifiez les prévisions dans votre région\n" +
//             "- Adaptez l'arrosage et la protection des cultures\n" +
//             "- Nos capteurs peuvent également vous aider à suivre l'humidité du sol",
//             6,
//             "météo", "temps", "pluie", "température", "climat", "prévisions"
//         ));

//         // Capteurs
//         INTENTS.put("sensors", new IntentData(
//             "📡 Nos capteurs permettent :\n" +
//             "- Suivi de l'humidité du sol\n" +
//             "- Mesure de la luminosité\n" +
//             "- Suivi de la température\n" +
//             "- Aide à la décision pour arrosage et fertilisation",
//             6,
//             "capteur", "sensor", "mesure", "dispositif", "surveillance"
//         ));

//         // ========== MALADIES SPÉCIFIQUES - OIGNON ==========
//         INTENTS.put("onion_alternaria", new IntentData(
//             "⚠️ Alternaria sur l'oignon provoque des taches brunes/noires concentriques sur les feuilles, " +
//             "favorisées par l'humidité. Rotation des cultures et fongicides adaptés sont recommandés.",
//             5,
//             "alternaria oignon", "alternaria", "tache oignon", "oignon malade"
//         ));

//         INTENTS.put("onion_downy", new IntentData(
//             "💧 Mildiou de l'oignon : taches jaunes sur les feuilles avec duvet gris à l'arrière. " +
//             "Humidité élevée favorise la maladie. Utilisez variétés résistantes et fongicides.",
//             5,
//             "mildiou oignon", "downy oignon", "duvet oignon", "mildiou"
//         ));

//         INTENTS.put("onion_botrytis", new IntentData(
//             "🌿 Pourriture grise (Botrytis) : taches grises sur feuilles et bulbes. " +
//             "Favorisée par temps humide. Enlevez les parties infectées et appliquez fongicides.",
//             5,
//             "botrytis oignon", "pourriture grise oignon", "tache grise oignon","botrytis"
//         ));

//         // ========== MALADIES SPÉCIFIQUES - ARACHIDE ==========
//         INTENTS.put("peanut_leaf_spot", new IntentData(
//             "🌱 Tache foliaire de l'arachide : petites taches sombres sur les feuilles, " +
//             "pouvant provoquer leur chute et réduire le rendement. Rotation des cultures et fongicides recommandés.",
//             5,
//             "tache arachide", "tache foliaire arachide", "arachide malade", "leaf spot"
//         ));

//         INTENTS.put("peanut_rust", new IntentData(
//             "🟠 Rouille de l'arachide : pustules rouges sur la face inférieure des feuilles. " +
//             "Réduit la croissance et le rendement. Utiliser variétés résistantes et traitements fongicides.",
//             5,
//             "rouille arachide", "rust arachide", "pustule arachide", "rouille"
//         ));

//         INTENTS.put("peanut_mildew", new IntentData(
//             "💧 Mildiou de l'arachide : taches jaunes puis brunes sur feuilles, surtout par temps humide. " +
//             "Prévention : semences saines et fongicides adaptés.",
//             5,
//             "mildiou arachide", "downy mildew arachide"
//         ));

//         // ========== MALADIES SPÉCIFIQUES - RIZ ==========
//         INTENTS.put("rice_blast", new IntentData(
//             "🔥 Blast du riz : taches brun-gris sur feuilles et talles, souvent en forme d'œil. " +
//             "Réduit le rendement. Utiliser variétés résistantes et bonnes pratiques culturales.",
//             5,
//             "blast riz", "pyriculariose", "riz malade", "tache riz","blast"
//         ));

//         INTENTS.put("rice_brown_spot", new IntentData(
//             "⚫ Tache brune du riz : petites taches brunes sur les feuilles, surtout jeunes plants. " +
//             "Fertilisation équilibrée et semences saines recommandées.",
//             5,
//             "tache brune riz", "brown spot riz", "helminthosporiose","tache brune"
//         ));

//         INTENTS.put("rice_mildew", new IntentData(
//             "💧 Mildiou du riz : taches chlorotiques sur feuilles puis grisâtres avec duvet. " +
//             "Favorisé par humidité élevée. Prévention : semences saines et traitement fongicide.",
//             5,
//             "mildiou riz", "downy mildew riz"
//         ));
//     }

//     public ChatMessageResponse processMessage(ChatMessageRequest request) {
//         // Sauvegarder le message de l'utilisateur
//         ChatMessage userMessage = ChatMessage.builder()
//                 .message(request.getMessage())
//                 .isUserMessage(true)
//                 .timestamp(LocalDateTime.now())
//                 .build();
        
//         chatMessageRepository.save(userMessage);

//         // Analyser l'intention et générer une réponse
//         String response = generateResponse(request.getMessage().toLowerCase().trim());

//         // Sauvegarder la réponse du bot
//         ChatMessage botMessage = ChatMessage.builder()
//                 .message(response)
//                 .isUserMessage(false)
//                 .timestamp(LocalDateTime.now())
//                 .build();
        
//         chatMessageRepository.save(botMessage);

//         return ChatMessageResponse.builder()
//                 .message(response)
//                 .timestamp(LocalDateTime.now())
//                 .type("text")
//                 // .sessionId(request.getSessionId())
//                 .build();
//     }

//     private String generateResponse(String message) {
//         log.debug("Processing message: {}", message);

//         // Normaliser le message (enlever accents, ponctuation excessive, etc.)
//         String normalizedMessage = normalizeText(message);

//         // Trouver les intents correspondants avec leur score
//         List<IntentMatch> matches = new ArrayList<>();

//         for (Map.Entry<String, IntentData> entry : INTENTS.entrySet()) {
//             IntentData intent = entry.getValue();
//             int matchScore = calculateMatchScore(normalizedMessage, intent);
            
//             if (matchScore > 0) {
//                 matches.add(new IntentMatch(entry.getKey(), intent, matchScore));
//             }
//         }

//         // Trier par score décroissant (score + priorité)
//         matches.sort((a, b) -> {
//             int scoreCompare = Integer.compare(b.totalScore(), a.totalScore());
//             if (scoreCompare != 0) return scoreCompare;
//             return Integer.compare(b.intent.priority, a.intent.priority);
//         });

//         // Retourner la meilleure correspondance
//         if (!matches.isEmpty()) {
//             IntentMatch bestMatch = matches.get(0);
//             log.info("Best match: {} with score: {}", bestMatch.intentKey, bestMatch.totalScore());
//             return bestMatch.intent.response;
//         }

//         // Réponse par défaut
//         return "🤔 Je comprends votre question. Pour une réponse précise, " +
//                "pourriez-vous préciser si vous souhaitez des informations sur :\n" +
//                "• La détection de maladies 🔍\n" +
//                "• L'identification de plantes 🌱\n" +
//                "• Le suivi de vos cultures 📊\n" +
//                "• Des conseils d'entretien 💡";
//     }

//     /**
//      * Calcule un score de correspondance entre le message et un intent
//      */
//     private int calculateMatchScore(String message, IntentData intent) {
//         int score = 0;

//         for (String keyword : intent.keywords) {
//             // Normaliser le mot-clé
//             String normalizedKeyword = normalizeText(keyword);
            
//             // Correspondance exacte du mot-clé (score élevé)
//             if (message.equals(normalizedKeyword)) {
//                 score += 100;
//             }
//             // Le message contient le mot-clé complet
//             else if (message.contains(normalizedKeyword)) {
//                 score += 50;
//             }
//             // Le mot-clé contient plusieurs mots et tous sont présents
//             else if (normalizedKeyword.contains(" ")) {
//                 String[] keywordParts = normalizedKeyword.split("\\s+");
//                 int partsFound = 0;
//                 for (String part : keywordParts) {
//                     if (message.contains(part)) {
//                         partsFound++;
//                     }
//                 }
//                 // Si tous les mots du mot-clé sont présents
//                 if (partsFound == keywordParts.length) {
//                     score += 40;
//                 }
//                 // Si au moins la moitié des mots sont présents
//                 else if (partsFound >= keywordParts.length / 2) {
//                     score += 20;
//                 }
//             }
//             // Correspondance partielle (le message contient au moins 3 caractères du mot-clé)
//             else if (normalizedKeyword.length() >= 3) {
//                 for (int i = 0; i <= normalizedKeyword.length() - 3; i++) {
//                     String substring = normalizedKeyword.substring(i, i + 3);
//                     if (message.contains(substring)) {
//                         score += 10;
//                         break;
//                     }
//                 }
//             }
//         }

//         return score;
//     }

//     /**
//      * Normalise le texte pour améliorer la correspondance
//      */
//     private String normalizeText(String text) {
//         if (text == null) return "";
        
//         // Convertir en minuscules
//         text = text.toLowerCase();
        
//         // Enlever les accents
//         text = text.replaceAll("[éèêë]", "e")
//                    .replaceAll("[àâä]", "a")
//                    .replaceAll("[îï]", "i")
//                    .replaceAll("[ôö]", "o")
//                    .replaceAll("[ùûü]", "u")
//                    .replaceAll("ç", "c");
        
//         // Enlever la ponctuation excessive mais garder les espaces
//         text = text.replaceAll("[^a-z0-9\\s]", " ");
        
//         // Réduire les espaces multiples
//         text = text.replaceAll("\\s+", " ").trim();
        
//         return text;
//     }

//     /**
//      * Classe interne pour stocker les correspondances avec leur score
//      */
//     private static class IntentMatch {
//         String intentKey;
//         IntentData intent;
//         int matchScore;

//         IntentMatch(String intentKey, IntentData intent, int matchScore) {
//             this.intentKey = intentKey;
//             this.intent = intent;
//             this.matchScore = matchScore;
//         }

//         int totalScore() {
//             return matchScore + (intent.priority * 10);
//         }
//     }

//     public void saveProblemReport(ProblemReportRequest request) {
//         ProblemReport report = ProblemReport.builder()
//                 .description(request.getDescription())
//                 .timestamp(LocalDateTime.now())
//                 .status("PENDING")
//                 .build();
        
//         problemReportRepository.save(report);
        
//         log.info("Problem report saved: {}", request.getDescription());
//     }
// }



package com.agriapp.service;

import com.agriapp.dto.ChatMessageRequest;
import com.agriapp.dto.ChatMessageResponse;
import com.agriapp.dto.ProblemReportRequest;
import com.agriapp.entity.ChatMessage;
import com.agriapp.entity.ProblemReport;
import com.agriapp.repository.ChatMessageRepository;
import com.agriapp.repository.ProblemReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProblemReportRepository problemReportRepository;

    private static final Map<String, IntentData> INTENTS = new HashMap<>();

    static class IntentData {
        String response;
        List<String> keywords;
        int priority;

        IntentData(String response, int priority, String... keywords) {
            this.response = response;
            this.keywords = Arrays.asList(keywords);
            this.priority = priority;
        }
    }

    static {
        // Salutations (priorité haute)
        INTENTS.put("greeting", new IntentData(
            "👋 Bonjour ! Je suis votre assistant AgriApp. " +
            "Je peux vous aider avec la détection de maladies, " +
            "l'identification de plantes, le suivi de vos cultures et bien plus. " +
            "Comment puis-je vous aider aujourd'hui ?",
            10,
            "bonjour", "salut", "hello", "hi", "bonsoir", "hey", "Coucou"
        ));

        // Remerciements
        INTENTS.put("thanks", new IntentData(
            "😊 Avec plaisir ! N'hésitez pas si vous avez d'autres questions. " +
            "Je suis là pour vous aider !",
            10,
            "merci", "thanks", "merci beaucoup", "merci bien", "merci infiniment"
        ));

        // Au revoir
        INTENTS.put("goodbye", new IntentData(
            "👋 Au revoir ! Bonne continuation pour vos cultures. " +
            "Revenez me voir quand vous voulez.",
            10,
            "au revoir", "bye", "a bientot", "a plus", "tchao", "ciao", "bonne journee"
        ));

        // Aide générale
        INTENTS.put("help", new IntentData(
            "📚 Je peux vous aider avec :\n" +
            "🔍 La détection de maladies des plantes\n" +
            "🌱 L'identification de cultures\n" +
            "📊 Le suivi intelligent de vos plantations\n" +
            "💡 Des conseils personnalisés\n\n" +
            "Posez-moi vos questions ou utilisez les suggestions ci-dessus !",
            9,
            "aide", "help", "assistance", "guide"
        ));

        // Détection de maladies
        INTENTS.put("disease_detection", new IntentData(
            "🔍 Pour détecter une maladie, rendez-vous dans la section 'Détection' " +
            "et téléchargez une photo claire de votre plante. Notre IA analysera l'image " +
            "et vous fournira un diagnostic précis avec des recommandations de traitement.",
            8,
            "maladie", "malade", "disease", "detecter maladie", "diagnostic", "symptomes", "probleme plante"
        ));

        // Identification de plantes
        INTENTS.put("plant_identification", new IntentData(
            "🌱 Pour identifier une plante, allez dans la section 'Classification' " +
            "et prenez une photo de la plante. L'IA vous indiquera l'espèce avec les " +
            "probabilités détaillées.",
            8,
            "identifier", "identification", "quelle plante", "espece", "classification", "reconnaitre plante"
        ));

        // Suivi des cultures
        INTENTS.put("crop_monitoring", new IntentData(
            "📊 Dans la section 'Suivi', vous pouvez enregistrer les données de vos cultures : " +
            "hauteur, stade de croissance, état sanitaire, etc. Visualisez l'évolution en " +
            "graphiques et obtenez des prédictions intelligentes.",
            7,
            "suivi", "monitoring", "suivre culture", "evolution", "graphique", "statistiques"
        ));

        // Arrosage
        INTENTS.put("watering", new IntentData(
            "💧 Conseils d'arrosage :\n" +
            "- Arrosez tôt le matin ou en soirée\n" +
            "- Évitez de mouiller les feuilles\n" +
            "- Adaptez la fréquence selon la météo\n" +
            "- Utilisez nos capteurs d'humidité pour un suivi optimal",
            7,
            "arrosage", "arroser", "irrigation", "eau", "irriguer", "humidite"
        ));

        // Engrais / Fertilisation
        INTENTS.put("fertilization", new IntentData(
            "🌿 Conseils sur les engrais :\n" +
            "- Utilisez des engrais adaptés à chaque type de culture\n" +
            "- Respectez les doses recommandées\n" +
            "- Appliquez selon le stade de croissance\n" +
            "- Consultez notre section recommandations pour plus de détails\n" +
            "💡 Fertilisation optimale : Équilibrez l'azote, le phosphore et le potassium",
            7,
            "engrais", "fertilisation", "fertiliser", "nutriment", "azote", "phosphore", "potassium", "npk"
        ));

        // Récolte
        INTENTS.put("harvest", new IntentData(
            "🌾 Pour la récolte :\n" +
            "- Vérifiez la maturité de votre culture\n" +
            "- Récoltez tôt le matin pour une meilleure conservation\n" +
            "- Suivez nos conseils spécifiques selon le type de plante",
            7,
            "recolte", "recolter", "harvest", "maturite", "quand recolter"
        ));

        // Conseils généraux
        INTENTS.put("general_advice", new IntentData(
            "💡 Je peux vous donner des conseils sur :\n" +
            "- Arrosage et irrigation\n" +
            "- Engrais et fertilisation\n" +
            "- Protection contre les maladies\n" +
            "- Optimisation du suivi des cultures",
            6,
            "conseil", "recommandation", "astuce", "suggestion"
        ));

        // Météo
        INTENTS.put("weather", new IntentData(
            "☀️ Pour la météo :\n" +
            "- Vérifiez les prévisions dans votre région\n" +
            "- Adaptez l'arrosage et la protection des cultures\n" +
            "- Nos capteurs peuvent également vous aider à suivre l'humidité du sol",
            6,
            "meteo", "temps", "pluie", "temperature", "climat", "previsions"
        ));

        // Capteurs
        INTENTS.put("sensors", new IntentData(
            "📡 Nos capteurs permettent :\n" +
            "- Suivi de l'humidité du sol\n" +
            "- Mesure de la luminosité\n" +
            "- Suivi de la température\n" +
            "- Aide à la décision pour arrosage et fertilisation",
            6,
            "capteur", "sensor", "mesure", "dispositif", "surveillance"
        ));

        // ========== MALADIES SPÉCIFIQUES - OIGNON ==========
        INTENTS.put("onion_alternaria", new IntentData(
            "⚠️ Alternaria sur l'oignon provoque des taches brunes/noires concentriques sur les feuilles, " +
            "favorisées par l'humidité. Rotation des cultures et fongicides adaptés sont recommandés.",
            8,
            "alternaria oignon", "alternaria", "tache oignon", "oignon malade"
        ));

        INTENTS.put("onion_downy", new IntentData(
            "💧 Mildiou de l'oignon : taches jaunes sur les feuilles avec duvet gris à l'arrière. " +
            "Humidité élevée favorise la maladie. Utilisez variétés résistantes et fongicides.",
            8,
            "mildiou oignon", "downy oignon", "duvet oignon"
        ));

        INTENTS.put("onion_botrytis", new IntentData(
            "🌿 Pourriture grise (Botrytis) : taches grises sur feuilles et bulbes. " +
            "Favorisée par temps humide. Enlevez les parties infectées et appliquez fongicides.",
            8,
            "botrytis oignon", "pourriture grise oignon", "tache grise oignon", "botrytis"
        ));

        // ========== MALADIES SPÉCIFIQUES - ARACHIDE ==========
        INTENTS.put("peanut_leaf_spot", new IntentData(
            "🌱 Tache foliaire de l'arachide : petites taches sombres sur les feuilles, " +
            "pouvant provoquer leur chute et réduire le rendement. Rotation des cultures et fongicides recommandés.",
            8,
            "tache arachide", "tache foliaire arachide", "arachide malade", "leaf spot arachide"
        ));

        INTENTS.put("peanut_rust", new IntentData(
            "🟠 Rouille de l'arachide : pustules rouges sur la face inférieure des feuilles. " +
            "Réduit la croissance et le rendement. Utiliser variétés résistantes et traitements fongicides.",
            8,
            "rouille arachide", "rust arachide", "pustule arachide"
        ));

        INTENTS.put("peanut_mildew", new IntentData(
            "💧 Mildiou de l'arachide : taches jaunes puis brunes sur feuilles, surtout par temps humide. " +
            "Prévention : semences saines et fongicides adaptés.",
            8,
            "mildiou arachide", "downy mildew arachide"
        ));

        // ========== MALADIES SPÉCIFIQUES - RIZ ==========
        INTENTS.put("rice_blast", new IntentData(
            "🔥 Blast du riz : taches brun-gris sur feuilles et talles, souvent en forme d'œil. " +
            "Réduit le rendement. Utiliser variétés résistantes et bonnes pratiques culturales.",
            8,
            "blast riz", "pyriculariose", "riz malade", "tache riz", "blast"
        ));

        INTENTS.put("rice_brown_spot", new IntentData(
            "⚫ Tache brune du riz : petites taches brunes sur les feuilles, surtout jeunes plants. " +
            "Fertilisation équilibrée et semences saines recommandées.",
            8,
            "tache brune riz", "brown spot riz", "helminthosporiose", "tache brune"
        ));

        INTENTS.put("rice_mildew", new IntentData(
            "💧 Mildiou du riz : taches chlorotiques sur feuilles puis grisâtres avec duvet. " +
            "Favorisé par humidité élevée. Prévention : semences saines et traitement fongicide.",
            8,
            "mildiou riz", "downy mildew riz"
        ));

        // ========== MALADIES GÉNÉRIQUES ==========
        INTENTS.put("generic_rust", new IntentData(
            "🟠 La rouille est une maladie fongique courante qui se manifeste par :\n" +
            "- Pustules ou taches de couleur rouille/orange sur les feuilles\n" +
            "- Affaiblit la plante et réduit le rendement\n" +
            "- Se propage par temps humide\n\n" +
            "💡 Traitement :\n" +
            "- Utilisez des variétés résistantes\n" +
            "- Appliquez des fongicides adaptés\n" +
            "- Assurez une bonne circulation d'air\n" +
            "- Éliminez les feuilles infectées",
            9,
            "rouille", "rust"
        ));

        INTENTS.put("generic_mildew", new IntentData(
            "💧 Le mildiou est une maladie grave causée par temps humide :\n" +
            "- Taches jaunes puis brunes sur les feuilles\n" +
            "- Duvet grisâtre à l'arrière des feuilles\n" +
            "- Peut détruire rapidement une culture\n\n" +
            "💡 Prévention :\n" +
            "- Utilisez des semences saines\n" +
            "- Espacez bien les plants\n" +
            "- Traitez préventivement avec des fongicides\n" +
            "- Évitez l'arrosage sur les feuilles",
            9,
            "mildiou", "mildew", "downy"
        ));


        // ========== LISTES DE MALADIES PAR CULTURE ==========
        INTENTS.put("rice_diseases_list", new IntentData(
            "🌾 **Principales maladies du riz :**\n\n" +
            "🔥 **Blast (Pyriculariose)** - Taches brun-gris en forme d'œil sur les feuilles\n" +
            "⚫ **Tache brune (Helminthosporiose)** - Petites taches brunes, surtout sur jeunes plants\n" +
            "💧 **Mildiou** - Taches chlorotiques puis grisâtres avec duvet\n" +
            "🦠 **Bactériose** - Stries translucides puis brunes sur les feuilles\n" +
            "🟡 **Jaunisse** - Jaunissement et nanisme des plants\n\n" +
            "💡 Pour plus de détails sur une maladie spécifique, demandez-moi (exemple: 'c'est quoi le blast ?')",
            8,
            "maladies riz", "maladie riz", "maladies du riz", "riz maladies", "pathologies riz"
        ));

        INTENTS.put("onion_diseases_list", new IntentData(
            "🧅 **Principales maladies de l'oignon :**\n\n" +
            "⚠️ **Alternaria** - Taches brunes/noires concentriques sur les feuilles\n" +
            "💧 **Mildiou** - Taches jaunes avec duvet gris à l'arrière des feuilles\n" +
            "🌿 **Botrytis (Pourriture grise)** - Taches grises sur feuilles et bulbes\n" +
            "🟣 **Fusariose** - Pourriture basale du bulbe avec décoloration\n" +
            "🦠 **Bactériose** - Pourriture molle et malodorante des bulbes\n\n" +
            "💡 Pour plus de détails sur une maladie spécifique, demandez-moi (exemple: 'c'est quoi l'alternaria ?')",
            8,
            "maladies oignon", "maladie oignon", "maladies de l oignon", "oignon maladies", "pathologies oignon"
        ));

        INTENTS.put("peanut_diseases_list", new IntentData(
            "🥜 **Principales maladies de l'arachide :**\n\n" +
            "🌱 **Tache foliaire (Cercosporiose)** - Petites taches sombres provoquant la chute des feuilles\n" +
            "🟠 **Rouille** - Pustules rouges/oranges sur la face inférieure des feuilles\n" +
            "💧 **Mildiou** - Taches jaunes puis brunes, favorisé par l'humidité\n" +
            "🟤 **Pourriture des gousses (Rhizoctonia)** - Attaque les gousses dans le sol\n" +
            "🦠 **Flétrissement bactérien** - Flétrissement rapide de la plante\n" +
            "🌿 **Rosette** - Nanisme et touffes de petites feuilles\n\n" +
            "💡 Pour plus de détails sur une maladie spécifique, demandez-moi (exemple: 'c'est quoi la rouille de l'arachide ?')",
            8,
            "maladies arachide", "maladie arachide", "maladies de l arachide", "arachide maladies", "pathologies arachide"
        ));

        INTENTS.put("all_crops_diseases", new IntentData(
            "📋 **Maladies des cultures principales :**\n\n" +
            "🌾 **RIZ** - Blast, Tache brune, Mildiou, Bactériose\n" +
            "🧅 **OIGNON** - Alternaria, Mildiou, Botrytis, Fusariose\n" +
            "🥜 **ARACHIDE** - Tache foliaire, Rouille, Mildiou, Rosette\n\n" +
            "💡 Pour voir les détails d'une culture spécifique, demandez :\n" +
            "• 'Quelles sont les maladies du riz ?'\n" +
            "• 'Maladies de l'oignon'\n" +
            "• 'Maladies de l'arachide'",
            7,
            "liste maladies", "toutes les maladies", "maladies cultures", "quelles maladies", "liste des maladies"
        ));
    }

    public ChatMessageResponse processMessage(ChatMessageRequest request) {
        ChatMessage userMessage = ChatMessage.builder()
                .message(request.getMessage())
                .isUserMessage(true)
                .timestamp(LocalDateTime.now())
                .build();
        
        chatMessageRepository.save(userMessage);

        String response = generateResponse(request.getMessage().toLowerCase().trim());

        ChatMessage botMessage = ChatMessage.builder()
                .message(response)
                .isUserMessage(false)
                .timestamp(LocalDateTime.now())
                .build();
        
        chatMessageRepository.save(botMessage);

        return ChatMessageResponse.builder()
                .message(response)
                .timestamp(LocalDateTime.now())
                .type("text")
                .build();
    }

    private String generateResponse(String message) {
        log.debug("Processing message: {}", message);

        String normalizedMessage = normalizeText(message);
        List<IntentMatch> matches = new ArrayList<>();

        for (Map.Entry<String, IntentData> entry : INTENTS.entrySet()) {
            IntentData intent = entry.getValue();
            int matchScore = calculateMatchScore(normalizedMessage, intent);
            
            if (matchScore > 0) {
                matches.add(new IntentMatch(entry.getKey(), intent, matchScore));
            }
        }

        matches.sort((a, b) -> {
            int scoreCompare = Integer.compare(b.totalScore(), a.totalScore());
            if (scoreCompare != 0) return scoreCompare;
            return Integer.compare(b.intent.priority, a.intent.priority);
        });

        if (!matches.isEmpty()) {
            IntentMatch bestMatch = matches.get(0);
            log.info("Best match: {} with score: {}", bestMatch.intentKey, bestMatch.totalScore());
            return bestMatch.intent.response;
        }

        return "🤔 Je comprends votre question. Pour une réponse précise, " +
               "pourriez-vous préciser si vous souhaitez des informations sur :\n" +
               "• La détection de maladies 🔍\n" +
               "• L'identification de plantes 🌱\n" +
               "• Le suivi de vos cultures 📊\n" +
               "• Des conseils d'entretien 💡";
    }

    private int calculateMatchScore(String message, IntentData intent) {
        int score = 0;
        String[] messageWords = message.split("\\s+");

        for (String keyword : intent.keywords) {
            String normalizedKeyword = normalizeText(keyword);
            
            // Score 1: Correspondance exacte du message entier
            if (message.equals(normalizedKeyword)) {
                score += 150;
                continue;
            }
            
            // Score 2: Le mot-clé exact apparaît comme mot complet dans le message
            if (isCompleteWordMatch(message, normalizedKeyword)) {
                score += 100;
                continue;
            }
            
            // Score 3: Pour les mots-clés composés (ex: "rouille arachide")
            if (normalizedKeyword.contains(" ")) {
                String[] keywordParts = normalizedKeyword.split("\\s+");
                int partsFound = 0;
                int exactMatches = 0;
                
                for (String part : keywordParts) {
                    if (isCompleteWordMatch(message, part)) {
                        partsFound++;
                        exactMatches++;
                    } else if (message.contains(part)) {
                        partsFound++;
                    }
                }
                
                // Tous les mots présents avec au moins un match exact
                if (partsFound == keywordParts.length && exactMatches > 0) {
                    score += 80;
                }
                // Tous les mots présents
                else if (partsFound == keywordParts.length) {
                    score += 50;
                }
                // Au moins la moitié des mots
                else if (partsFound >= keywordParts.length / 2) {
                    score += 25;
                }
                continue;
            }
            
            // Score 4: Le message contient le mot-clé (substring)
            if (message.contains(normalizedKeyword)) {
                score += 40;
                continue;
            }
            
            // Score 5: Correspondance partielle faible (au moins 4 caractères)
            if (normalizedKeyword.length() >= 4) {
                for (int i = 0; i <= normalizedKeyword.length() - 4; i++) {
                    String substring = normalizedKeyword.substring(i, Math.min(i + 4, normalizedKeyword.length()));
                    if (message.contains(substring)) {
                        score += 5;
                        break;
                    }
                }
            }
        }

        return score;
    }

    /**
     * Vérifie si un mot-clé apparaît comme mot complet dans le message
     */
    private boolean isCompleteWordMatch(String message, String keyword) {
        String pattern = "\\b" + keyword + "\\b";
        return message.matches(".*" + pattern + ".*");
    }

    private String normalizeText(String text) {
        if (text == null) return "";
        
        text = text.toLowerCase();
        text = text.replaceAll("[éèêë]", "e")
                   .replaceAll("[àâä]", "a")
                   .replaceAll("[îï]", "i")
                   .replaceAll("[ôö]", "o")
                   .replaceAll("[ùûü]", "u")
                   .replaceAll("ç", "c");
        text = text.replaceAll("[^a-z0-9\\s]", " ");
        text = text.replaceAll("\\s+", " ").trim();
        
        return text;
    }

    private static class IntentMatch {
        String intentKey;
        IntentData intent;
        int matchScore;

        IntentMatch(String intentKey, IntentData intent, int matchScore) {
            this.intentKey = intentKey;
            this.intent = intent;
            this.matchScore = matchScore;
        }

        int totalScore() {
            return matchScore + (intent.priority * 10);
        }
    }

    public void saveProblemReport(ProblemReportRequest request) {
        ProblemReport report = ProblemReport.builder()
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .status("PENDING")
                .build();
        
        problemReportRepository.save(report);
        log.info("Problem report saved: {}", request.getDescription());
    }
}