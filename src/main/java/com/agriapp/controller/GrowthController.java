package com.agriapp.controller;

import com.agriapp.model.GrowthRecord;
import com.agriapp.model.Plant;
import com.agriapp.repository.GrowthRepository;
import com.agriapp.repository.PlantRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/growth")
public class GrowthController {

    private final GrowthRepository growthRepository;
    private final PlantRepository plantRepository;

    public GrowthController(GrowthRepository growthRepository, PlantRepository plantRepository) {
        this.growthRepository = growthRepository;
        this.plantRepository = plantRepository;
    }

    @GetMapping("/{plantId}")
    public List<GrowthRecord> getGrowthByPlant(@PathVariable Long plantId) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plante non trouvée"));
        return growthRepository.findByPlant(plant);
    }

    @PostMapping
    public GrowthRecord addGrowth(@RequestBody GrowthRecord record) {
        record.setDate(LocalDate.now());
        return growthRepository.save(record);
    }
}
