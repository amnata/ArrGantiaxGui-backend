package com.agriapp.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.agriapp.repository.PlantRepository;
import com.agriapp.model.Plant;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository repository;

    public List<Plant> getAll() { return repository.findAll(); }
    public Plant create(Plant p) { return repository.save(p); }
}
