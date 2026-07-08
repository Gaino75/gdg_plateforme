package com.gdg.admin.controller;

import com.gdg.admin.dto.DashboardDTO;
import com.gdg.admin.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")

public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Tableau de bord global
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        return ResponseEntity.ok(
            dashboardService.getDashboard());
    }
}