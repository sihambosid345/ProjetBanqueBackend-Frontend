import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { AccountsService } from '../services/accounts.service';
import { Observable, catchError, throwError } from 'rxjs';
import { AccountDetails } from '../model/account.model';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']

})
export class AccountsComponent implements OnInit {
  accountFormGroup!: FormGroup;
  operationsFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize: number = 5;
  accountObservable!: Observable<AccountDetails>;
  errorMessage!: string;

  constructor(
    private fb: FormBuilder,
    private accountsService: AccountsService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Formulaire de recherche
    this.accountFormGroup = this.fb.group({
      accountId: ['', Validators.required]
    });

    // Formulaire des opérations
    this.operationsFormGroup = this.fb.group({
      operationType: ['DEBIT', Validators.required],
      amount: [0, [Validators.required, Validators.min(1)]],
      description: ['', Validators.required],
      accountDestination: ['']
    });

    // Récupérer l'ID depuis l'URL (si présent)
    const accountId = this.route.snapshot.params['accountId'];

    if (accountId) {
      console.log('✅ Account ID détecté dans l\'URL:', accountId);
      // Pré-remplir le champ et charger automatiquement
      this.accountFormGroup.patchValue({ accountId: accountId });
      this.handleSearchAccount();
    }
  }

  handleSearchAccount() {
    const accountId = this.accountFormGroup.value.accountId;
    this.errorMessage = '';

    this.accountObservable = this.accountsService
      .getAccount(accountId, this.currentPage, this.pageSize)
      .pipe(
        catchError((err) => {
          this.errorMessage = err.error?.message || 'Compte introuvable';
          return throwError(() => err);
        })
      );
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation() {
    const accountId = this.accountFormGroup.value.accountId;
    const operationType = this.operationsFormGroup.value.operationType;
    const amount = this.operationsFormGroup.value.amount;
    const description = this.operationsFormGroup.value.description;
    const accountDestination = this.operationsFormGroup.value.accountDestination;

    if (operationType === 'DEBIT') {
      this.accountsService.debit(accountId, amount, description).subscribe({
        next: () => {
          alert('Débit effectué avec succès');
          this.operationsFormGroup.reset({ operationType: 'DEBIT' });
          this.handleSearchAccount();
        },
        error: (err) => alert('Erreur: ' + err.error?.message)
      });
    } else if (operationType === 'CREDIT') {
      this.accountsService.credit(accountId, amount, description).subscribe({
        next: () => {
          alert('Crédit effectué avec succès');
          this.operationsFormGroup.reset({ operationType: 'DEBIT' });
          this.handleSearchAccount();
        },
        error: (err) => alert('Erreur: ' + err.error?.message)
      });
    } else if (operationType === 'TRANSFER') {
      if (!accountDestination) {
        alert('Veuillez saisir le compte de destination');
        return;
      }
      this.accountsService.transfer(accountId, accountDestination, amount).subscribe({
        next: () => {
          alert('Virement effectué avec succès');
          this.operationsFormGroup.reset({ operationType: 'DEBIT' });
          this.handleSearchAccount();
        },
        error: (err) => alert('Erreur: ' + err.error?.message)
      });
    }
  }
}
