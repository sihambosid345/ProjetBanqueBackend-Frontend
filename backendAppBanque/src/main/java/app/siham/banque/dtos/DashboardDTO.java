package app.siham.banque.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private int totalClients;
    private int totalComptes;
    private int comptesEpargne;
    private int comptesCourant;
    private double soldeTotal;
    private Map<String, Double> soldeParClient; // Nom client -> Solde total
}