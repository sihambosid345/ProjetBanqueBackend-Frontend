package app.siham.banque.repository;
import app.siham.banque.entity.Client;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE c.nom LIKE :kw")
    List<Client> searchClient(@Param("kw") String keyword);
}