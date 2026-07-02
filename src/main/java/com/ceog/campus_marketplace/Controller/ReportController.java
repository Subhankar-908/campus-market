package com.ceog.campus_marketplace.Controller;

import com.ceog.campus_marketplace.Model.Report;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Service.ReportService;
import com.ceog.campus_marketplace.WebSecurity.JwtUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private JwtUntil jwtUntil;

    //add report
    @PostMapping("/addReport/{productId}")
    public ResponseEntity<String> addReport(@PathVariable Long productId,@RequestBody Report report){
            User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            reportService.addReport(user.getId(), productId, report);
            return ResponseEntity.ok("report successfully");
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something else");
    }


    @GetMapping("/getreport")
    public ResponseEntity<?> getReport(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "20")   int size,
            @RequestParam(defaultValue = "id")   String sortBy,      // 👈 changed from uploadDate
            @RequestParam(defaultValue = "desc") String direction) {

        List<String> allowed = List.of("id", "reason", "reportDate"); // 👈 whitelist
        String safeSortBy = allowed.contains(sortBy) ? sortBy : "id";

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(safeSortBy).ascending()
                : Sort.by(safeSortBy).descending();

        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sort);

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(reportService.getAllReport(pageable, user.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //delete report
    @DeleteMapping("/deleteReport/{reporedUsertId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reporedUsertId) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            reportService.deleteReport(user.getId(), reporedUsertId);
            return ResponseEntity.ok("Report deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // Or BAD_REQUEST
        }
    }
}
