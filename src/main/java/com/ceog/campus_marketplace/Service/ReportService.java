package com.ceog.campus_marketplace.Service;

import com.ceog.campus_marketplace.Dto.ReportDto;
import com.ceog.campus_marketplace.Model.Product;
import com.ceog.campus_marketplace.Model.Report;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Repository.ProductRepository;
import com.ceog.campus_marketplace.Repository.ReportRepository;
import com.ceog.campus_marketplace.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;


    //----------------add report----------------
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void addReport(Long reporterId,Long productId,Report report){
        User reporter=userRepository.findById(reporterId)
                .orElseThrow(()->new RuntimeException("reporter not found"));
        Product p=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("product not founde"));
        User reported=userRepository.findById(p.getSeller().getId())
                .orElseThrow(()->new RuntimeException("reported user not found"));
        Report report1=new Report();
        report1.setReason(report.getReason());
        report1.setReportDate(LocalDateTime.now());
        report1.setReporter(reporter);
        report1.setReportedUser(reported);
        report1.setProductTitle(p.getTitle());
        report1.setProductId(p.getId());

        reportRepository.save(report1);
    }

    //-----------------get report ---------------
    @PreAuthorize("hasAnyRole( 'ADMIN','SUPER_ADMIN')")
    public Page<ReportDto> getAllReport(Pageable pageable, Long userId){
        return reportRepository.findAll(pageable)
                .map(this::toDto);

    }


    //-----------------delete report---------------
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','SUPER_ADMIN')")
    public void deleteReport(Long reporterId, Long reportUserId) {
        // Add logging for debugging
        System.out.println("Attempting to delete report: reporterId=" + reporterId + ", reportUserId=" + reportUserId);

        Report report = reportRepository.findByReporterIdAndReportedUserId(reporterId, reportUserId)
                .orElseThrow(() -> new RuntimeException("Report not found for the given reporter and reported user"));

        // No need for extra auth check here— the find already ensures reporterId matches
        // If you want admin override, add it separately, but it's not necessary

        reportRepository.delete(report);
        System.out.println("Report deleted successfully");
    }

    public ReportDto toDto(Report report) {
        return ReportDto.builder()
                .id(report.getId())
                .reason(report.getReason())
                .reportDate(report.getReportDate())
                .productTitle(report.getProductTitle())
                .productTitle(report.getProductTitle())
                .reporterId(report.getReporter() != null ? report.getReporter().getId() : null)
                .repoterName(report.getReporter() != null ? report.getReporter().getUsername() : null)
                .reportedUserId(report.getReportedUser() != null ? report.getReportedUser().getId() : null)
                .reportedUserName(report.getReportedUser() != null ? report.getReportedUser().getUsername() : null)
                .build();
    }
}
