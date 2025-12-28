package app.siham.banque.web;

import app.siham.banque.dtos.CompteBancaireDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import app.siham.banque.dtos.ClientDTO;
import app.siham.banque.exceptions.ClientNotFoundException;
import app.siham.banque.service.BankService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ClientRestController {

    private final BankService bankService;


    @GetMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientDTO> clients() {
        return bankService.listClients();
    }


    @GetMapping("/clients/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientDTO> searchClients(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return bankService.searchClients("%" + keyword + "%");
    }

    @GetMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientDTO getClient(@PathVariable("id") Long clientId) throws ClientNotFoundException {
        return bankService.getClient(clientId);
    }

    @PostMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientDTO saveClient(@RequestBody ClientDTO clientDTO) {
        return bankService.saveClient(clientDTO);
    }


    @PutMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientDTO updateClient(@PathVariable("id") Long clientId, @RequestBody ClientDTO clientDTO) {
        clientDTO.setId(clientId);
        return bankService.updateClient(clientDTO);
    }

    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClient(@PathVariable("id") Long id) {
        bankService.deleteClient(id);
    }


    @GetMapping("/clients/{id}/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CompteBancaireDTO> getAccountsByClient(@PathVariable Long id) throws ClientNotFoundException {
        ClientDTO client = bankService.getClient(id);
        if (client == null) throw new ClientNotFoundException("Client not found with id " + id);

        return bankService.getAccountsByClient(id);
    }

}