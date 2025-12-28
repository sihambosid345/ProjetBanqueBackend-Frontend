package app.siham.banque.entity;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue; // <-- ajouté
import lombok.*;

@Entity
@DiscriminatorValue("CE")
@Data @NoArgsConstructor @AllArgsConstructor
public class CompteEpargne extends CompteBancaire {
    private double tauxInteret;
}
