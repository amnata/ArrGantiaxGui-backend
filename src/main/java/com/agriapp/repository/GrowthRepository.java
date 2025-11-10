package com.agriapp.repository;

import com.agriapp.model.GrowthRecord;
import com.agriapp.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowthRepository extends JpaRepository<GrowthRecord, Long> {
    List<GrowthRecord> findByPlant(Plant plant);
}
