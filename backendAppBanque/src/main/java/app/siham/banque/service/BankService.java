package app.siham.banque.service;

import app.siham.banque.dtos.*;
import app.siham.banque.exceptions.ClientNotFoundException;
import app.siham.banque.exceptions.CompteBancaireNotFoundException;
import app.siham.banque.exceptions.SoldeNotSufficentException;

import java.util.List;

public interface BankService {

    ClientDTO saveClient(ClientDTO clientDTO);

    CompteCourantDTO saveCompteCourantBancaire(double initialBalance, double decouvert, Long clientId)
            throws ClientNotFoundException;

    CompteEpargneDTO saveCompteEpargneBancaire(double initialBalance, double tauxInteret, Long clientId)
            throws ClientNotFoundException;

    List<ClientDTO> listClients();

    CompteBancaireDTO getCompteBancaire(String compteId) throws CompteBancaireNotFoundException;

    void debit(String compteId, double montant, String description)
            throws CompteBancaireNotFoundException, SoldeNotSufficentException;

    void credit(String compteId, double montant, String description)
            throws CompteBancaireNotFoundException;

    void virement(String compteIdSource, String compteIdDestination, double montant)
            throws CompteBancaireNotFoundException, SoldeNotSufficentException;

    List<CompteBancaireDTO> listCompteBancaire();

    ClientDTO getClient(Long clientId) throws ClientNotFoundException;

    ClientDTO updateClient(ClientDTO clientDTO);

    void deleteClient(Long clientId);

    List<AccountOperationDTO> accountHistory(String compteId)throws CompteBancaireNotFoundException;

    AccountHistoryDTO getAccountHistory(String compteId, int page, int size)
            throws CompteBancaireNotFoundException;

    List<ClientDTO> searchClients(String keyword);

    List<CompteBancaireDTO> getAccountsByClient(Long clientId) throws ClientNotFoundException;
    DashboardDTO getDashboardStats();
}


