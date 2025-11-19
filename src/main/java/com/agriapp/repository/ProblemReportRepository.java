package com.agriapp.repository;

import com.agriapp.entity.ProblemReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProblemReportRepository extends JpaRepository<ProblemReport, Long> {
    List<ProblemReport> findByStatusOrderByTimestampDesc(String status);
    List<ProblemReport> findByUserIdOrderByTimestampDesc(Long userId);
}