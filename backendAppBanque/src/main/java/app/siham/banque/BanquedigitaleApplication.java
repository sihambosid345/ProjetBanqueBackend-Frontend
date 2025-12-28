package app.siham.banque;

import app.siham.banque.dtos.ClientDTO;
import app.siham.banque.dtos.CompteBancaireDTO;
import app.siham.banque.dtos.CompteCourantDTO;
import app.siham.banque.dtos.CompteEpargneDTO;
import app.siham.banque.exceptions.ClientNotFoundException;
import app.siham.banque.exceptions.CompteBancaireNotFoundException;
import app.siham.banque.exceptions.SoldeNotSufficentException;
import app.siham.banque.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class BanquedigitaleApplication {

    public static void main(String[] args) {
        System.out.println("L'application Banque démarre...");
        SpringApplication.run(BanquedigitaleApplication.class, args);
        System.out.println("Application démarrée avec succès !");
    }

    @Bean
    CommandLineRunner start(BankService bankService) {
        return args -> {

            // ✅ CORRECTION : empêcher la duplication des données
            if (!bankService.listClients().isEmpty()) {
                System.out.println("📌 Données déjà présentes — initialisation ignorée");
                return;
            }

            // Création des clients
            Stream.of("Youssef", "Siham").forEach(name -> {
                ClientDTO clientDTO = new ClientDTO();
                clientDTO.setNom(name);
                clientDTO.setEmail(name + "@gmail.com");
                bankService.saveClient(clientDTO);
            });

            // Création des comptes pour chaque client
            bankService.listClients().forEach(client -> {
                try {
                    bankService.saveCompteCourantBancaire(
                            Math.random() * 90000,
                            9000,
                            client.getId()
                    );
                    bankService.saveCompteEpargneBancaire(
                            Math.random() * 120000,
                            5.5,
                            client.getId()
                    );
                } catch (ClientNotFoundException e) {
                    e.printStackTrace();
                }
            });

            // Effectuer des opérations aléatoires sur les comptes
            List<CompteBancaireDTO> comptes = bankService.listCompteBancaire();
            for (CompteBancaireDTO compte : comptes) {
                for (int i = 0; i < 10; i++) {
                    String compteId;

                    if (compte instanceof CompteEpargneDTO) {
                        compteId = ((CompteEpargneDTO) compte).getId();
                    } else {
                        compteId = ((CompteCourantDTO) compte).getId();
                    }

                    try {
                        bankService.credit(
                                compteId,
                                10000 + Math.random() * 120000,
                                "Credit"
                        );
                        bankService.debit(
                                compteId,
                                1000 + Math.random() * 9000,
                                "Debit"
                        );
                    } catch (CompteBancaireNotFoundException | SoldeNotSufficentException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
