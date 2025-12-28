package app.siham.banque.service;

import app.siham.banque.dtos.*;
import app.siham.banque.entity.*;
import app.siham.banque.exceptions.ClientNotFoundException;
import app.siham.banque.exceptions.CompteBancaireNotFoundException;
import app.siham.banque.exceptions.SoldeNotSufficentException;
import app.siham.banque.repository.ClientRepository;
import app.siham.banque.repository.CompteRepository;
import app.siham.banque.repository.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import app.siham.banque.dtos.*;
import app.siham.banque.entity.*;
import app.siham.banque.exceptions.*;
import app.siham.banque.mappers.BankAccountMapperImpl;
import app.siham.banque.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {

    private final ClientRepository clientRepository;
    private final CompteRepository compteBancaireRepository;
    private final OperationRepository operationRepository;
    private final BankAccountMapperImpl dtoMapper;

    // ---------------- Clients ----------------
    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        log.info("Saving new Client");
        Client client = dtoMapper.fromClientDTO(clientDTO);
        Client savedClient = clientRepository.save(client);
        return dtoMapper.fromClient(savedClient);
    }

    @Override
    public List<ClientDTO> listClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(dtoMapper::fromClient).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClient(Long clientId) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        return dtoMapper.fromClient(client);
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO) {
        Client client = dtoMapper.fromClientDTO(clientDTO);
        Client savedClient = clientRepository.save(client);
        return dtoMapper.fromClient(savedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public List<ClientDTO> searchClients(String keyword) {
        List<Client> clients = clientRepository.searchClient(keyword);
        return clients.stream().map(dtoMapper::fromClient).collect(Collectors.toList());
    }

    // ---------------- Comptes ----------------
    @Override
    public CompteCourantDTO saveCompteCourantBancaire(double initialBalance, double decouvert, Long clientId)
            throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        CompteCourant compte = new CompteCourant();
        compte.setId(UUID.randomUUID().toString());
        compte.setDateCreation(new Date());
        compte.setSolde(initialBalance);
        compte.setDecouvert(decouvert);
        compte.setClient(client);
        CompteCourant saved = compteBancaireRepository.save(compte);
        return dtoMapper.fromCompteCourant(saved);
    }

    @Override
    public CompteEpargneDTO saveCompteEpargneBancaire(double initialBalance, double tauxInteret, Long clientId)
            throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        CompteEpargne compte = new CompteEpargne();
        compte.setId(UUID.randomUUID().toString());
        compte.setDateCreation(new Date());
        compte.setSolde(initialBalance);
        compte.setTauxInteret(tauxInteret);
        compte.setClient(client);
        CompteEpargne saved = compteBancaireRepository.save(compte);
        return dtoMapper.fromCompteEpargne(saved);
    }

    @Override
    public CompteBancaireDTO getCompteBancaire(String compteId) throws CompteBancaireNotFoundException {
        CompteBancaire compte = compteBancaireRepository.findById(compteId)
                .orElseThrow(() -> new CompteBancaireNotFoundException("Compte not found"));
        if (compte instanceof CompteCourant)
            return dtoMapper.fromCompteCourant((CompteCourant) compte);
        else
            return dtoMapper.fromCompteEpargne((CompteEpargne) compte);
    }

    @Override
    public List<CompteBancaireDTO> listCompteBancaire() {
        List<CompteBancaire> comptes = compteBancaireRepository.findAll();
        return comptes.stream().map(c -> {
            if (c instanceof CompteCourant) return dtoMapper.fromCompteCourant((CompteCourant) c);
            else return dtoMapper.fromCompteEpargne((CompteEpargne) c);
        }).collect(Collectors.toList());
    }

    // ---------------- Opérations ----------------
    @Override
    public void debit(String compteId, double montant, String description)
            throws CompteBancaireNotFoundException, SoldeNotSufficentException {
        CompteBancaire compte = compteBancaireRepository.findById(compteId)
                .orElseThrow(() -> new CompteBancaireNotFoundException("Compte not found"));
        if (compte.getSolde() < montant)
            throw new SoldeNotSufficentException("Solde insuffisant");
        Operation op = new Operation();
        op.setType(TypeOp.DEBIT);
        op.setMontant(montant);
        op.setDescription(description);
        op.setDateOp(new Date());
        op.setCompte(compte);
        operationRepository.save(op);
        compte.setSolde(compte.getSolde() - montant);
        compteBancaireRepository.save(compte);
    }

    @Override
    public void credit(String compteId, double montant, String description)
            throws CompteBancaireNotFoundException {
        CompteBancaire compte = compteBancaireRepository.findById(compteId)
                .orElseThrow(() -> new CompteBancaireNotFoundException("Compte not found"));
        Operation op = new Operation();
        op.setType(TypeOp.CREDIT);
        op.setMontant(montant);
        op.setDescription(description);
        op.setDateOp(new Date());
        op.setCompte(compte);
        operationRepository.save(op);
        compte.setSolde(compte.getSolde() + montant);
        compteBancaireRepository.save(compte);
    }

    @Override
    public void virement(String compteIdSource, String compteIdDestination, double montant)
            throws CompteBancaireNotFoundException, SoldeNotSufficentException {
        debit(compteIdSource, montant, "Virement vers " + compteIdDestination);
        credit(compteIdDestination, montant, "Virement depuis " + compteIdSource);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String compteId) throws CompteBancaireNotFoundException {
        CompteBancaire compte = compteBancaireRepository.findById(compteId)
                .orElseThrow(() -> new CompteBancaireNotFoundException("Compte introuvable"));
        List<Operation> ops = operationRepository.findByCompte(compte); // utilise le compte, pas l'ID Long
        return ops.stream().map(dtoMapper::fromOperation).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String compteId, int page, int size)
            throws CompteBancaireNotFoundException {
        CompteBancaire compte = compteBancaireRepository.findById(compteId)
                .orElseThrow(() -> new CompteBancaireNotFoundException("Compte introuvable"));
        Page<Operation> operations = operationRepository
                .findByCompteOrderByDateOpDesc(compte, PageRequest.of(page, size));

        AccountHistoryDTO history = new AccountHistoryDTO();
        List<AccountOperationDTO> opsDTO = operations.getContent().stream()
                .map(dtoMapper::fromOperation)
                .collect(Collectors.toList());

        history.setAccountOperationDTOS(opsDTO);
        history.setAccountId(compte.getId());
        history.setBalance(compte.getSolde());
        history.setCurrentPage(page);
        history.setPageSize(size);
        history.setTotalPages(operations.getTotalPages());

        return history;
    }
    @Override
    public List<CompteBancaireDTO> getAccountsByClient(Long clientId)
            throws ClientNotFoundException {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        List<CompteBancaire> comptes = compteBancaireRepository.findByClient(client);

        return comptes.stream().map(c -> {
            if (c instanceof CompteCourant)
                return dtoMapper.fromCompteCourant((CompteCourant) c);
            else
                return dtoMapper.fromCompteEpargne((CompteEpargne) c);
        }).collect(Collectors.toList());
    }
    @Override
    public DashboardDTO getDashboardStats() {
        DashboardDTO stats = new DashboardDTO();

        // Total clients
        stats.setTotalClients((int) clientRepository.count());

        // Total comptes
        List<CompteBancaire> allComptes = compteBancaireRepository.findAll();
        stats.setTotalComptes(allComptes.size());

        // Compter par type
        long comptesEpargne = allComptes.stream()
                .filter(c -> c instanceof CompteEpargne)
                .count();
        stats.setComptesEpargne((int) comptesEpargne);
        stats.setComptesCourant(allComptes.size() - (int) comptesEpargne);

        // Solde total
        double soldeTotal = allComptes.stream()
                .mapToDouble(CompteBancaire::getSolde)
                .sum();
        stats.setSoldeTotal(soldeTotal);

        // Solde par client
        Map<String, Double> soldeParClient = new HashMap<>();
        for (Client client : clientRepository.findAll()) {
            double soldeClient = compteBancaireRepository.findByClient(client).stream()
                    .mapToDouble(CompteBancaire::getSolde)
                    .sum();
            soldeParClient.put(client.getNom(), soldeClient);
        }
        stats.setSoldeParClient(soldeParClient);

        return stats;
    }
}
