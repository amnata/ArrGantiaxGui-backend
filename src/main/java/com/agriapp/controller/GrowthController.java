// package com.agriapp.controller;

// import com.agriapp.model.GrowthRecord;
// import com.agriapp.model.Plant;
// import com.agriapp.repository.GrowthRepository;
// import com.agriapp.repository.PlantRepository;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.util.List;

// @RestController
// @RequestMapping("/growth")
// public class GrowthController {

//     private final GrowthRepository growthRepository;
//     private final PlantRepository plantRepository;

//     public GrowthController(GrowthRepository growthRepository, PlantRepository plantRepository) {
//         this.growthRepository = growthRepository;
//         this.plantRepository = plantRepository;
//     }

//     @GetMapping("/{plantId}")
//     public List<GrowthRecord> getGrowthByPlant(@PathVariable Long plantId) {
//         Plant plant = plantRepository.findById(plantId)
//                 .orElseThrow(() -> new RuntimeException("Plante non trouvée"));
//         return growthRepository.findByPlant(plant);
//     }

//     @PostMapping
//     public GrowthRecord addGrowth(@RequestBody GrowthRecord record) {
//         record.setDate(LocalDate.now());
//         return growthRepository.save(record);
//     }
// }


package com.agriapp.controller;

import com.agriapp.model.GrowthRecord;
import com.agriapp.model.Plant;
import com.agriapp.repository.GrowthRepository;
import com.agriapp.repository.PlantRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/growth")
@CrossOrigin(origins = "http://localhost:4200")
public class GrowthController {

    private final GrowthRepository growthRepository;
    private final PlantRepository plantRepository;
    private final RestTemplate restTemplate;

    private final String FLASK_BASE_URL = "http://localhost:5000";

    public GrowthController(GrowthRepository growthRepository, PlantRepository plantRepository) {
        this.growthRepository = growthRepository;
        this.plantRepository = plantRepository;
        this.restTemplate = new RestTemplate();
    }

    // =============================================
    // ENDPOINTS CORRESPONDANT À FLASK
    // =============================================

    /**
     * Correspond à : POST /growth/predict (Flask)
     */
    @PostMapping("/predict")
    public ResponseEntity<?> predictGrowth(@RequestBody GrowthPredictionRequest request) {
        try {
            // ✅ CORRECTION : Utiliser le cropType de la requête directement
            String flaskUrl = FLASK_BASE_URL + "/growth/predict";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<GrowthPredictionRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            Map<String, Object> fallback = Map.of(
                    "success", false,
                    "error", "Service de prédiction indisponible: " + e.getMessage(),
                    "fallback_used", true
            );
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
        }
    }

    /**
     * Correspond à : POST /growth/analyze (Flask)
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeGrowth(@RequestBody GrowthAnalysisRequest request) {
        try {
            // Récupérer la plante
            Plant plant = plantRepository.findById(request.getPlantId())
                    .orElseThrow(() -> new RuntimeException("Plante non trouvée"));

            // ✅ CORRECTION : Vérifier si cropType existe
            String cropType = plant.getCropType() != null ? plant.getCropType() : "arachide";

            // Récupérer les données historiques
            List<GrowthRecord> historicalRecords = growthRepository.findByPlantOrderByDateAsc(plant);

            // Préparer les données pour Flask
            Map<String, Object> flaskRequest = new HashMap<>();
            flaskRequest.put("plant_data", Map.of(
                    "height", request.getCurrentHeight(),
                    "plant_type", cropType.toLowerCase() // ✅ UTILISER cropType
            ));
            flaskRequest.put("sensor_data", request.getSensorData());
            flaskRequest.put("historical_data", convertToSensorDataList(historicalRecords));

            // Appeler Flask
            String flaskUrl = FLASK_BASE_URL + "/growth/analyze";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(flaskRequest, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            Map<String, Object> fallback = Map.of(
                    "success", false,
                    "error", "Service d'analyse indisponible: " + e.getMessage(),
                    "fallback_used", true
            );
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
        }
    }

    /**
     * Correspond à : GET /growth/model/info (Flask)
     */
    @GetMapping("/model/info")
    public ResponseEntity<?> getGrowthModelInfo() {
        try {
            String flaskUrl = FLASK_BASE_URL + "/growth/model/info";
            ResponseEntity<Map> response = restTemplate.getForEntity(flaskUrl, Map.class);
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            Map<String, Object> fallback = Map.of(
                    "growth_model_loaded", false,
                    "message", "Impossible de contacter le service de modèles",
                    "error", e.getMessage()
            );
            return ResponseEntity.ok(fallback);
        }
    }

    // =============================================
    // ENDPOINTS SPRING BOOT COMPLÉMENTAIRES
    // =============================================

    /**
     * Endpoint combiné : Crée un enregistrement + prédiction
     */
    @PostMapping("/record-with-prediction")
    public ResponseEntity<?> createRecordWithPrediction(@RequestBody GrowthRecord record) {
        try {
            // 1. Sauvegarder l'enregistrement
            Plant plant = plantRepository.findById(record.getPlant().getId())
                    .orElseThrow(() -> new RuntimeException("Plante non trouvée"));

            record.setPlant(plant);
            record.setDate(LocalDate.now());
            GrowthRecord savedRecord = growthRepository.save(record);

            // 2. Récupérer l'historique pour la prédiction
            List<GrowthRecord> historicalRecords = growthRepository.findByPlantOrderByDateAsc(plant);

            // 3. ✅ CORRECTION : Utiliser cropType sécurisé
            String cropType = plant.getCropType() != null ? plant.getCropType() : "arachide";

            // 4. Préparer la prédiction Flask
            GrowthPredictionRequest predictionRequest = new GrowthPredictionRequest();
            predictionRequest.setSequenceData(convertToSensorDataList(historicalRecords));
            predictionRequest.setCurrentHeight(record.getHeight());
            predictionRequest.setPlantType(cropType.toLowerCase());

            // 5. Appeler la prédiction
            ResponseEntity<Map> predictionResponse = restTemplate.postForEntity(
                    FLASK_BASE_URL + "/growth/predict",
                    new HttpEntity<>(predictionRequest, createHeaders()),
                    Map.class
            );

            // 6. Combiner les résultats
            Map<String, Object> result = new HashMap<>();
            result.put("growth_record", savedRecord);
            result.put("prediction", predictionResponse.getBody());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur création enregistrement: " + e.getMessage()));
        }
    }

    /**
     * Endpoint de base pour créer un enregistrement (sans prédiction)
     */
    @PostMapping
    public ResponseEntity<GrowthRecord> createGrowthRecord(@RequestBody GrowthRecord record) {
        try {
            Plant plant = plantRepository.findById(record.getPlant().getId())
                    .orElseThrow(() -> new RuntimeException("Plante non trouvée"));

            record.setPlant(plant);
            record.setDate(LocalDate.now());
            GrowthRecord savedRecord = growthRepository.save(record);

            return ResponseEntity.ok(savedRecord);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupérer tous les enregistrements d'une plante
     */
    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<GrowthRecord>> getGrowthByPlant(@PathVariable Long plantId) {
        try {
            Plant plant = plantRepository.findById(plantId)
                    .orElseThrow(() -> new RuntimeException("Plante non trouvée"));
            List<GrowthRecord> records = growthRepository.findByPlantOrderByDateAsc(plant);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =============================================
    // METHODES UTILITAIRES CORRIGÉES
    // =============================================

    private List<Map<String, Object>> convertToSensorDataList(List<GrowthRecord> records) {
        List<Map<String, Object>> sensorDataList = new ArrayList<>();
        for (GrowthRecord record : records) {
            sensorDataList.add(convertToSensorData(record));
        }
        return sensorDataList;
    }

    private Map<String, Object> convertToSensorData(GrowthRecord record) {
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("chlorophyll_content", record.getChlorophyllContent() != null ? record.getChlorophyllContent() : 35.0);
        sensorData.put("ambient_temperature", record.getAmbientTemperature() != null ? record.getAmbientTemperature() : 25.0);
        sensorData.put("soil_temperature", record.getSoilTemperature() != null ? record.getSoilTemperature() : 23.0);
        sensorData.put("humidity", record.getHumidity() != null ? record.getHumidity() : 60.0);
        sensorData.put("light_intensity", record.getLightIntensity() != null ? record.getLightIntensity() : 1200.0);
        sensorData.put("electrochemical_signal", record.getElectrochemicalSignal() != null ? record.getElectrochemicalSignal() : 0.5);
        return sensorData;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // =============================================
    // CLASSES DE REQUÊTES
    // =============================================

    public static class GrowthPredictionRequest {
        private List<Map<String, Object>> sequenceData;
        private Double currentHeight;
        private String plantType;

        // Getters et Setters
        public List<Map<String, Object>> getSequenceData() { return sequenceData; }
        public void setSequenceData(List<Map<String, Object>> sequenceData) { this.sequenceData = sequenceData; }
        public Double getCurrentHeight() { return currentHeight; }
        public void setCurrentHeight(Double currentHeight) { this.currentHeight = currentHeight; }
        public String getPlantType() { return plantType; }
        public void setPlantType(String plantType) { this.plantType = plantType; }
    }

    public static class GrowthAnalysisRequest {
        private Long plantId;
        private Double currentHeight;
        private Map<String, Object> sensorData;

        // Getters et Setters
        public Long getPlantId() { return plantId; }
        public void setPlantId(Long plantId) { this.plantId = plantId; }
        public Double getCurrentHeight() { return currentHeight; }
        public void setCurrentHeight(Double currentHeight) { this.currentHeight = currentHeight; }
        public Map<String, Object> getSensorData() { return sensorData; }
        public void setSensorData(Map<String, Object> sensorData) { this.sensorData = sensorData; }
    }
}