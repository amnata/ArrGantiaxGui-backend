package com.agriapp.service;

import com.agriapp.model.GrowthRecord;
import com.agriapp.model.Plant;
import com.agriapp.repository.GrowthRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrowthService {

    private final GrowthRepository growthRepository;

    public GrowthService(GrowthRepository growthRepository) {
        this.growthRepository = growthRepository;
    }

    /**
     * Prépare les données pour la prédiction LSTM
     * Format attendu par le modèle Flask
     */
    public List<Map<String, Object>> prepareLSTMSequence(Long plantId, int sequenceLength) {
        List<Object[]> sensorData = growthRepository.findLastSensorSequence(plantId, sequenceLength);

        return sensorData.stream().map(data -> {
            Map<String, Object> sequenceItem = new HashMap<>();
            sequenceItem.put("chlorophyll_content", data[0] != null ? data[0] : 35.0);
            sequenceItem.put("ambient_temperature", data[1] != null ? data[1] : 25.0);
            sequenceItem.put("soil_temperature", data[2] != null ? data[2] : 23.0);
            sequenceItem.put("humidity", data[3] != null ? data[3] : 60.0);
            sequenceItem.put("light_intensity", data[4] != null ? data[4] : 1200.0);
            sequenceItem.put("electrochemical_signal", data[5] != null ? data[5] : 0.5);
            return sequenceItem;
        }).collect(Collectors.toList());
    }

    /**
     * Calcule les statistiques de croissance pour une plante
     */
    public Map<String, Object> calculateGrowthStats(Long plantId) {
        Map<String, Object> stats = new HashMap<>();

        // Statistiques de base
        Double avgHeight = growthRepository.findAverageHeightByPlantId(plantId).orElse(0.0);
        Double maxHeight = growthRepository.findMaxHeightByPlantId(plantId).orElse(0.0);
        Double growthRate = growthRepository.findAverageGrowthRateByPlantId(plantId).orElse(0.0);

        stats.put("average_height", Math.round(avgHeight * 100.0) / 100.0);
        stats.put("max_height", maxHeight);
        stats.put("average_growth_rate", Math.round(growthRate * 100.0) / 100.0);

        // Évolution temporelle
        List<GrowthRecord> records = growthRepository.findByPlantId(plantId);
        if (!records.isEmpty()) {
            LocalDate firstDate = records.get(0).getDate();
            LocalDate lastDate = records.get(records.size() - 1).getDate();
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(firstDate, lastDate);

            stats.put("measurement_period_days", daysBetween);
            stats.put("total_measurements", records.size());
            stats.put("measurement_frequency", daysBetween > 0 ?
                    Math.round((records.size() / (double) daysBetween) * 100.0) / 100.0 : 0);
        }

        return stats;
    }

    /**
     * Détecte les anomalies de croissance
     */
    public List<Map<String, Object>> detectGrowthAnomalies(Long plantId) {
        List<Map<String, Object>> anomalies = new ArrayList<>();
        List<GrowthRecord> records = growthRepository.findByPlantId(plantId);

        if (records.size() < 2) return anomalies;

        // Détection des baisses soudaines
        for (int i = 1; i < records.size(); i++) {
            GrowthRecord current = records.get(i);
            GrowthRecord previous = records.get(i - 1);

            double heightChange = current.getHeight() - previous.getHeight();

            // Baise de plus de 20%
            if (heightChange < - (previous.getHeight() * 0.2)) {
                anomalies.add(Map.of(
                        "date", current.getDate(),
                        "type", "HAUTEUR_BAISSE",
                        "severity", "HIGH",
                        "current_height", current.getHeight(),
                        "previous_height", previous.getHeight(),
                        "change", heightChange
                ));
            }

            // Chlorophylle trop basse
            if (current.getChlorophyllContent() != null && current.getChlorophyllContent() < 25) {
                anomalies.add(Map.of(
                        "date", current.getDate(),
                        "type", "CHLOROPHYLLE_BASSE",
                        "severity", "MEDIUM",
                        "value", current.getChlorophyllContent()
                ));
            }
        }

        return anomalies;
    }
}