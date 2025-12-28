package app.siham.banque.entity;


import javax.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateOp;
    private double montant;
    @Enumerated(EnumType.STRING)
    private TypeOp type;
    @ManyToOne
    private CompteBancaire compte;
    private String performedByUserId;
    private String description;// id de l'utilisateur authentifié-description z3ma
}
