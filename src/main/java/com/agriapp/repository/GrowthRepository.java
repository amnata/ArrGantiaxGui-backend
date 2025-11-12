// package com.agriapp.repository;

// import com.agriapp.model.GrowthRecord;
// import com.agriapp.model.Plant;
// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;

// public interface GrowthRepository extends JpaRepository<GrowthRecord, Long> {
//     List<GrowthRecord> findByPlant(Plant plant);
// }


package com.agriapp.repository;

import com.agriapp.model.GrowthRecord;
import com.agriapp.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrowthRepository extends JpaRepository<GrowthRecord, Long> {

    // =============================================
    // REQUÊTES POUR LE SUIVI DE CROISSANCE
    // =============================================

    /**
     * Trouver tous les enregistrements d'une plante ordonnés par date
     * Correspond aux besoins de séquence LSTM
     */
    List<GrowthRecord> findByPlantOrderByDateAsc(Plant plant);

    /**
     * Trouver les enregistrements par type de culture
     * Pour l'analyse par type de plante
     */
    List<GrowthRecord> findByPlant_CropTypeOrderByDateAsc(String cropType);

    /**
     * Dernier enregistrement pour une plante
     * Pour les prédictions avec données récentes
     */
    Optional<GrowthRecord> findTopByPlantIdOrderByDateDesc(Long plantId);

    /**
     * Enregistrements entre deux dates pour une plante
     * Pour l'analyse de périodes spécifiques
     */
    List<GrowthRecord> findByPlantAndDateBetweenOrderByDateAsc(
            Plant plant, LocalDate startDate, LocalDate endDate);

    /**
     * Les N derniers enregistrements pour une plante
     * Pour les séquences LSTM (6 dernières mesures)
     */
    @Query("SELECT g FROM GrowthRecord g WHERE g.plant.id = :plantId ORDER BY g.date DESC LIMIT :limit")
    List<GrowthRecord> findLastNByPlantId(@Param("plantId") Long plantId, @Param("limit") int limit);

    /**
     * Enregistrements avec données de capteurs complètes
     * Pour les analyses LSTM qui nécessitent toutes les features
     */
    @Query("SELECT g FROM GrowthRecord g WHERE g.plant.id = :plantId " +
            "AND g.chlorophyllContent IS NOT NULL " +
            "AND g.ambientTemperature IS NOT NULL " +
            "AND g.soilTemperature IS NOT NULL " +
            "AND g.humidity IS NOT NULL " +
            "AND g.lightIntensity IS NOT NULL " +
            "AND g.electrochemicalSignal IS NOT NULL " +
            "ORDER BY g.date ASC")
    List<GrowthRecord> findCompleteSensorDataByPlantId(@Param("plantId") Long plantId);

    // =============================================
    // STATISTIQUES ET CALCULS
    // =============================================

    /**
     * Hauteur moyenne d'une plante
     */
    @Query("SELECT AVG(g.height) FROM GrowthRecord g WHERE g.plant.id = :plantId")
    Optional<Double> findAverageHeightByPlantId(@Param("plantId") Long plantId);

    /**
     * Hauteur maximale d'une plante
     */
    @Query("SELECT MAX(g.height) FROM GrowthRecord g WHERE g.plant.id = :plantId")
    Optional<Double> findMaxHeightByPlantId(@Param("plantId") Long plantId);

    /**
     * Taux de croissance moyen (cm/jour)
     */
    @Query("SELECT (MAX(g.height) - MIN(g.height)) / COUNT(DISTINCT g.date) " +
            "FROM GrowthRecord g WHERE g.plant.id = :plantId")
    Optional<Double> findAverageGrowthRateByPlantId(@Param("plantId") Long plantId);

    /**
     * Statistiques de santé moyennes
     */
    @Query("SELECT AVG(g.chlorophyllContent), AVG(g.ambientTemperature), AVG(g.humidity) " +
            "FROM GrowthRecord g WHERE g.plant.id = :plantId")
    Optional<Object[]> findAverageHealthMetricsByPlantId(@Param("plantId") Long plantId);

    /**
     * Dernières mesures de capteurs pour séquence LSTM
     * Retourne exactement les 6 features nécessaires au modèle
     */
    @Query("SELECT g.chlorophyllContent, g.ambientTemperature, g.soilTemperature, " +
            "g.humidity, g.lightIntensity, g.electrochemicalSignal " +
            "FROM GrowthRecord g WHERE g.plant.id = :plantId " +
            "ORDER BY g.date DESC LIMIT :sequenceLength")
    List<Object[]> findLastSensorSequence(@Param("plantId") Long plantId,
                                          @Param("sequenceLength") int sequenceLength);

    // =============================================
    // ANALYSES TEMPORELLES
    // =============================================

    /**
     * Évolution de la hauteur sur une période
     */
    @Query("SELECT g.date, g.height FROM GrowthRecord g " +
            "WHERE g.plant.id = :plantId AND g.date BETWEEN :startDate AND :endDate " +
            "ORDER BY g.date ASC")
    List<Object[]> findHeightEvolution(@Param("plantId") Long plantId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    /**
     * Détection des pics de croissance
     */
    @Query(value = """
        WITH growth_changes AS (
            SELECT date, height, 
                   height - LAG(height) OVER (ORDER BY date) as growth_change
            FROM growth_records 
            WHERE plant_id = :plantId
        )
        SELECT date, height, growth_change 
        FROM growth_changes 
        WHERE ABS(growth_change) > :threshold
        ORDER BY date DESC
        """, nativeQuery = true)
    List<Object[]> findGrowthSpikes(@Param("plantId") Long plantId,
                                    @Param("threshold") double threshold);

    /**
     * Statistiques par stade de croissance
     */
    @Query("SELECT g.stage, COUNT(g), AVG(g.height), AVG(g.chlorophyllContent) " +
            "FROM GrowthRecord g WHERE g.plant.id = :plantId " +
            "GROUP BY g.stage")
    List<Object[]> findStatsByGrowthStage(@Param("plantId") Long plantId);

    // =============================================
    // REQUÊTES POUR DASHBOARD
    // =============================================

    /**
     * Dernières mesures de toutes les plantes actives
     */
    @Query("SELECT g FROM GrowthRecord g WHERE g.date IN (" +
            "SELECT MAX(g2.date) FROM GrowthRecord g2 GROUP BY g2.plant.id)")
    List<GrowthRecord> findLatestMeasurementsForAllPlants();

    /**
     * Plantes nécessitant une attention (santé faible)
     */
    @Query("SELECT g FROM GrowthRecord g WHERE g.date IN (" +
            "SELECT MAX(g2.date) FROM GrowthRecord g2 GROUP BY g2.plant.id) " +
            "AND (g.healthStatus = 'FAIBLE' OR g.healthStatus = 'CRITIQUE')")
    List<GrowthRecord> findPlantsNeedingAttention();

    /**
     * Résumé croissance pour le dashboard
     */
    @Query(value = "SELECT p.id, p.name, p.cropType, " +
            "MAX(g.height) as maxHeight, " +
            "AVG(g.height) as avgHeight, " +
            "COUNT(g) as measurementCount, " +
            "MAX(g.date) as lastMeasurement " +
            "FROM Plant p LEFT JOIN GrowthRecord g ON p.id = g.plant.id " +
            "GROUP BY p.id, p.name, p.cropType")
    default List<Object[]> findGrowthSummaryForDashboard() {
        return null;
    }

    List<GrowthRecord> findByPlantId(Long plantId);
}