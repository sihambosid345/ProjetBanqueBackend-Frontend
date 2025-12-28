package app.siham.banque.repository;
import app.siham.banque.entity.CompteBancaire;
import app.siham.banque.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByCompte(CompteBancaire compte);

    Page<Operation> findByCompteOrderByDateOpDesc(CompteBancaire compte, Pageable pageable);
}
