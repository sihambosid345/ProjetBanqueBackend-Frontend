package app.siham.banque.repository;

import app.siham.banque.entity.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import app.siham.banque.entity.Client;
import java.util.List;

public interface CompteRepository extends JpaRepository<CompteBancaire, String> {
  List<CompteBancaire> findByClient(Client client);
}