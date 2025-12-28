package app.siham.banque.mappers;

import app.siham.banque.dtos.AccountOperationDTO;
import app.siham.banque.dtos.ClientDTO;
import app.siham.banque.dtos.CompteCourantDTO;
import app.siham.banque.dtos.CompteEpargneDTO;
import app.siham.banque.entity.Client;
import app.siham.banque.entity.CompteCourant;
import app.siham.banque.entity.CompteEpargne;
import app.siham.banque.entity.Operation;
import app.siham.banque.dtos.*;
import app.siham.banque.entity.*;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    // ------------------- Client -------------------
    public ClientDTO fromClient(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        return dto;
    }

    public Client fromClientDTO(ClientDTO dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        return client;
    }

    // ------------------- Compte Epargne -------------------
    public CompteEpargneDTO fromCompteEpargne(CompteEpargne compte) {
        CompteEpargneDTO dto = new CompteEpargneDTO();
        dto.setId(compte.getId());
        dto.setBalance(compte.getSolde());
        dto.setCreatedAt(compte.getDateCreation());
        dto.setInterestRate(compte.getTauxInteret());
        dto.setStatus(compte.getStatut());
        dto.setType("EPARGNE");
        // ✅ CORRECTION : utiliser clientdto au lieu de customerDTO
        if (compte.getClient() != null) {
            dto.setClientdto(fromClient(compte.getClient()));
        }
        return dto;
    }

    public CompteEpargne fromCompteEpargneDTO(CompteEpargneDTO dto) {
        CompteEpargne compte = new CompteEpargne();
        compte.setId(dto.getId());
        compte.setSolde(dto.getBalance());
        compte.setDateCreation(dto.getCreatedAt());
        compte.setTauxInteret(dto.getInterestRate());
        compte.setStatut(dto.getStatus());
        if (dto.getClientdto() != null) {
            compte.setClient(fromClientDTO(dto.getClientdto()));
        }
        return compte;
    }

    // ------------------- Compte Courant -------------------
    public CompteCourantDTO fromCompteCourant(CompteCourant compte) {
        CompteCourantDTO dto = new CompteCourantDTO();
        dto.setId(compte.getId());
        dto.setBalance(compte.getSolde());
        dto.setCreatedAt(compte.getDateCreation());
        dto.setOverDraft(compte.getDecouvert());
        dto.setStatus(compte.getStatut());
        dto.setType("COURANT");
        if (compte.getClient() != null) {
            dto.setClientdto(fromClient(compte.getClient()));
        }
        return dto;
    }

    public CompteCourant fromCompteCourantDTO(CompteCourantDTO dto) {
        CompteCourant compte = new CompteCourant();
        compte.setId(dto.getId());
        compte.setSolde(dto.getBalance());
        compte.setDateCreation(dto.getCreatedAt());
        compte.setDecouvert(dto.getOverDraft());
        compte.setStatut(dto.getStatus());
        if (dto.getClientdto() != null) {
            compte.setClient(fromClientDTO(dto.getClientdto()));
        }
        return compte;
    }

    // ------------------- Operation -------------------
    public AccountOperationDTO fromOperation(Operation operation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        dto.setId(operation.getId());
        dto.setAmount(operation.getMontant());
        dto.setOperationDate(operation.getDateOp());
        dto.setType(operation.getType());
        dto.setDescription(operation.getDescription());
        return dto;
    }
}