package app.siham.banque.entity;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("CC")
@Data @NoArgsConstructor @AllArgsConstructor
public class CompteCourant extends CompteBancaire {
    private Double decouvert;
}