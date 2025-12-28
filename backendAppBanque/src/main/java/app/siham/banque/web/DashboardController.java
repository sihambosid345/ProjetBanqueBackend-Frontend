package app.siham.banque.web;

import lombok.AllArgsConstructor;
import app.siham.banque.dtos.DashboardDTO;
import app.siham.banque.service.BankService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class DashboardController {

    private final BankService bankService;

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public DashboardDTO getDashboardStats() {
        return bankService.getDashboardStats();
    }
}