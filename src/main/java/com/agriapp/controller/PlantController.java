package com.agriapp.controller;

import com.agriapp.model.Plant;
import com.agriapp.repository.PlantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plants")
public class PlantController {

    private final PlantRepository plantRepository;

    public PlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    @PostMapping
    public Plant addPlant(@RequestBody Plant plant) {
        return plantRepository.save(plant);
    }
}
