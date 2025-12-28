package app.siham.banque.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompteCourantDTO extends CompteBancaireDTO {
    private double overDraft; // ✅ Spécifique au compte courant
}