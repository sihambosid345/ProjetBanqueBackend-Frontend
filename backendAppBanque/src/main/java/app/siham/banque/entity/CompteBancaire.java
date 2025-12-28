package app.siham.banque.entity;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import java.util.Date;
import javax.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type",length = 4)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class CompteBancaire {
    @Id
    private String id;
    private Date dateCreation;
    private double solde;
    @Enumerated(EnumType.STRING)
    private StatCompte statut;
    private String devise;

    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "compte",fetch = FetchType.LAZY)
    private List<Operation> operations;

    }
