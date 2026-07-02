package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    Optional<Report> findByReporterIdAndReportedUserId(Long reporterId, Long reportedUserId);

}
