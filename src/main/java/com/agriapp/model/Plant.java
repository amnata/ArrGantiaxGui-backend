// package com.agriapp.model;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;

// @Entity
// public class Plant {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;

//     public Plant() {}
//     public Plant(String name) { this.name = name; }

//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }

//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }
// }


package com.agriapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // ✅ AJOUTER CETTE PROPRIÉTÉ
    @Column(name = "crop_type", nullable = false)
    private String cropType; // ARACHIDE, OIGNON, RIZ

    @Column(name = "planting_date")
    private LocalDate plantingDate;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GrowthRecord> growthRecords = new ArrayList<>();

    // =============================================
    // CONSTRUCTEURS
    // =============================================

    public Plant() {}

    public Plant(String name, String cropType) {
        this.name = name;
        this.cropType = cropType;
        this.plantingDate = LocalDate.now();
    }

    // =============================================
    // GETTERS & SETTERS
    // =============================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // ✅ AJOUTER GETTER/SETTER POUR CROP_TYPE
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }

    public LocalDate getPlantingDate() { return plantingDate; }
    public void setPlantingDate(LocalDate plantingDate) { this.plantingDate = plantingDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<GrowthRecord> getGrowthRecords() { return growthRecords; }
    public void setGrowthRecords(List<GrowthRecord> growthRecords) { this.growthRecords = growthRecords; }
}