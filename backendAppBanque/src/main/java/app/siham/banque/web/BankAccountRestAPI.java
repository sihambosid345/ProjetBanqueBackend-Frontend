package app.siham.banque.web;

import app.siham.banque.dtos.*;
import app.siham.banque.exceptions.CompteBancaireNotFoundException;
import app.siham.banque.exceptions.SoldeNotSufficentException;
import app.siham.banque.service.BankService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class BankAccountRestAPI {

    private final BankService bankService;

    public BankAccountRestAPI(BankService bankService) {
        this.bankService = bankService;
    }

    // ===================== COMPTES =====================

    // ADMIN + USER
    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CompteBancaireDTO getBankAccount(@PathVariable String accountId)
            throws CompteBancaireNotFoundException {
        return bankService.getCompteBancaire(accountId);
    }

    // ADMIN
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CompteBancaireDTO> listAccounts() {
        return bankService.listCompteBancaire();
    }

    // ADMIN + USER
    @GetMapping("/accounts/{accountId}/operations")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId)
            throws CompteBancaireNotFoundException {
        return bankService.accountHistory(accountId);
    }

    // ADMIN + USER
    @GetMapping("/accounts/{accountId}/pageOperations")
    @PreAuthorize("hasRole('ADMIN')")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) throws CompteBancaireNotFoundException {
        return bankService.getAccountHistory(accountId, page, size);
    }

    // ===================== OPÉRATIONS =====================

    // ADMIN + USER
    @PostMapping("/accounts/debit")
    @PreAuthorize("hasRole('ADMIN')")
    public DebitDTO debit(@RequestBody DebitDTO dto)
            throws CompteBancaireNotFoundException, SoldeNotSufficentException {
        bankService.debit(dto.getId(), dto.getAmount(), dto.getDescription());
        return dto;
    }

    // ADMIN + USER
    @PostMapping("/accounts/credit")
    @PreAuthorize("hasRole('ADMIN')")
    public CreditDTO credit(@RequestBody CreditDTO dto)
            throws CompteBancaireNotFoundException {
        bankService.credit(dto.getAccountId(), dto.getAmount(), dto.getDescription());
        return dto;
    }

    // ADMIN + USER
    @PostMapping("/accounts/transfer")
    @PreAuthorize("hasRole('ADMIN')")
    public void transfer(@RequestBody TransferRequestDTO dto)
            throws CompteBancaireNotFoundException, SoldeNotSufficentException {
        bankService.virement(
                dto.getAccountSource(),
                dto.getAccountDestination(),
                dto.getAmount()
        );
    }
}
