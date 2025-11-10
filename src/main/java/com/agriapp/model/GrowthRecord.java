package com.agriapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class GrowthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    private LocalDate date;
    private Double height;
    private String notes;

    public GrowthRecord() {}

    public GrowthRecord(Plant plant, LocalDate date, Double height, String notes) {
        this.plant = plant;
        this.date = date;
        this.height = height;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
