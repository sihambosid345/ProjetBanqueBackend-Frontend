package app.siham.banque.dtos;

import lombok.Data;
import app.siham.banque.entity.StatCompte;
import java.util.Date;

@Data
public class CompteBancaireDTO {
    private String id;
    private String type; // "COURANT" ou "EPARGNE"
    private double balance;
    private Date createdAt;
    private StatCompte status;
    private ClientDTO clientdto; // ✅ Commun aux deux types de comptes
}