package app.siham.banque.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompteEpargneDTO extends CompteBancaireDTO {
    private double interestRate; // ✅ Spécifique au compte épargne
}