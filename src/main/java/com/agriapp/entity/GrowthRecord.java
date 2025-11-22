package com.agriapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "growth_records")
public class GrowthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "height_cm", nullable = false)
    private Double height;

    @Column(name = "growth_stage")
    private String stage; // GERMINATION, VEGETATION, FLORAISON, MATURATION

    @Column(name = "health_status")
    private String healthStatus; // EXCELLENT, BON, MOYEN, FAIBLE, CRITIQUE

    // =============================================
    // DONNÉES CAPTEURS POUR MODÈLE LSTM
    // =============================================

    @Column(name = "chlorophyll_content")
    private Double chlorophyllContent; // Unité SPAD

    @Column(name = "ambient_temperature")
    private Double ambientTemperature; // °C

    @Column(name = "soil_temperature")
    private Double soilTemperature; // °C

    @Column(name = "humidity")
    private Double humidity; // %

    @Column(name = "light_intensity")
    private Double lightIntensity; // lux

    @Column(name = "electrochemical_signal")
    private Double electrochemicalSignal; // mV

    @Column(length = 500)
    private String notes;

    // =============================================
    // MÉTADONNÉES
    // =============================================

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId; // <-- chaque mesure appartient à un utilisateur


    // =============================================
    // CONSTRUCTEURS
    // =============================================

    public GrowthRecord() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public GrowthRecord(Plant plant, Double height, String stage, String healthStatus) {
        this();
        this.plant = plant;
        this.date = LocalDate.now();
        this.height = height;
        this.stage = stage;
        this.healthStatus = healthStatus;
        this.userId = userId; 
    }

    // =============================================
    // GETTERS & SETTERS
    // =============================================

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public Double getChlorophyllContent() { return chlorophyllContent; }
    public void setChlorophyllContent(Double chlorophyllContent) { this.chlorophyllContent = chlorophyllContent; }

    public Double getAmbientTemperature() { return ambientTemperature; }
    public void setAmbientTemperature(Double ambientTemperature) { this.ambientTemperature = ambientTemperature; }

    public Double getSoilTemperature() { return soilTemperature; }
    public void setSoilTemperature(Double soilTemperature) { this.soilTemperature = soilTemperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public Double getLightIntensity() { return lightIntensity; }
    public void setLightIntensity(Double lightIntensity) { this.lightIntensity = lightIntensity; }

    public Double getElectrochemicalSignal() { return electrochemicalSignal; }
    public void setElectrochemicalSignal(Double electrochemicalSignal) { this.electrochemicalSignal = electrochemicalSignal; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}



// package com.agriapp.model;

// import jakarta.persistence.*;
// import java.time.LocalDate;

// @Entity
// @Table(name = "growth_records")
// public class GrowthRecord {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "plant_id", nullable = false)
//     private Plant plant;

//     @Column(nullable = false)
//     private LocalDate date;

//     @Column(name = "height_cm", nullable = false)
//     private Double height;

//     @Column(name = "growth_stage")
//     private String stage;

//     @Column(name = "health_status")
//     private String healthStatus;

//     // =============================================
//     // DONNÉES CAPTEURS
//     // =============================================
//     @Column(name = "chlorophyll_content")
//     private Double chlorophyllContent;

//     @Column(name = "ambient_temperature")
//     private Double ambientTemperature;

//     @Column(name = "soil_temperature")
//     private Double soilTemperature;

//     @Column(name = "humidity")
//     private Double humidity;

//     @Column(name = "light_intensity")
//     private Double lightIntensity;

//     @Column(name = "electrochemical_signal")
//     private Double electrochemicalSignal;

//     @Column(length = 500)
//     private String notes;

//     // =============================================
//     // MÉTADONNÉES
//     // =============================================
//     @Column(name = "created_at")
//     private LocalDate createdAt;

//     @Column(name = "updated_at")
//     private LocalDate updatedAt;

//     @Column(name = "user_id", nullable = false)
//     private Long userId;

//     // =============================================
//     // CONSTRUCTEURS
//     // =============================================
//     public GrowthRecord() {
//         this.createdAt = LocalDate.now();
//         this.updatedAt = LocalDate.now();
//     }

//     public GrowthRecord(Plant plant, Double height, String stage, String healthStatus, Long userId) {
//         this();
//         this.plant = plant;
//         this.date = LocalDate.now();
//         this.height = height;
//         this.stage = stage;
//         this.healthStatus = healthStatus;
//         this.userId = userId;
//     }

//     // =============================================
//     // GETTERS & SETTERS
//     // =============================================
//     @PreUpdate
//     public void preUpdate() {
//         this.updatedAt = LocalDate.now();
//     }

//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
//     public Plant getPlant() { return plant; }
//     public void setPlant(Plant plant) { this.plant = plant; }
//     public LocalDate getDate() { return date; }
//     public void setDate(LocalDate date) { this.date = date; }
//     public Double getHeight() { return height; }
//     public void setHeight(Double height) { this.height = height; }
//     public String getStage() { return stage; }
//     public void setStage(String stage) { this.stage = stage; }
//     public String getHealthStatus() { return healthStatus; }
//     public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
//     public Double getChlorophyllContent() { return chlorophyllContent; }
//     public void setChlorophyllContent(Double chlorophyllContent) { this.chlorophyllContent = chlorophyllContent; }
//     public Double getAmbientTemperature() { return ambientTemperature; }
//     public void setAmbientTemperature(Double ambientTemperature) { this.ambientTemperature = ambientTemperature; }
//     public Double getSoilTemperature() { return soilTemperature; }
//     public void setSoilTemperature(Double soilTemperature) { this.soilTemperature = soilTemperature; }
//     public Double getHumidity() { return humidity; }
//     public void setHumidity(Double humidity) { this.humidity = humidity; }
//     public Double getLightIntensity() { return lightIntensity; }
//     public void setLightIntensity(Double lightIntensity) { this.lightIntensity = lightIntensity; }
//     public Double getElectrochemicalSignal() { return electrochemicalSignal; }
//     public void setElectrochemicalSignal(Double electrochemicalSignal) { this.electrochemicalSignal = electrochemicalSignal; }
//     public String getNotes() { return notes; }
//     public void setNotes(String notes) { this.notes = notes; }
//     public LocalDate getCreatedAt() { return createdAt; }
//     public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
//     public LocalDate getUpdatedAt() { return updatedAt; }
//     public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
//     public Long getUserId() { return userId; }
//     public void setUserId(Long userId) { this.userId = userId; }
// }
