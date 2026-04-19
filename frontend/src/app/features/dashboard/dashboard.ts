import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BankService } from '../../core/services/bank.service';
import { Compte } from '../../shared/models/compte.model';
import { Operation } from '../../shared/models/operation.model';
import { ErrorResponse } from '../../shared/models/error-response.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  readonly Types = {
    COURANT: 'Compte Courant',
    LIVRET: 'Livret',
    DEFAULT: 'DEFAULT'
  };

  private bankService = inject(BankService);

  public compte = signal<Compte | null>(null);
  public operations = signal<Operation[]>([]);
  public loading = signal<boolean>(false);

  public operationErreur = signal<string>('');

  ngOnInit(): void {
  }

  public typeCompte = computed(() => {
    const c = this.compte();
    if (!c) return this.Types.DEFAULT;
    return c.detailsCompteCourant ? this.Types.COURANT : this.Types.LIVRET;
  });

  rechercher(numero: string): void {
    if (!numero) return;

    this.loading.set(true);

    this.bankService.getCompte(numero).subscribe({
      next: (data) =>{
        this.compte.set(data)
      },
      error: (err: ErrorResponse) => {
        console.error(`Code ${err.status}: ${err.message}`);
        this.compte.set(null);
      }
    });

    this.bankService.getReleve(numero).subscribe({
      next: (data) => {
        this.operations.set(data);
        this.loading.set(false);
      },
      error: (err: ErrorResponse) => {
        console.error(`Code ${err.status}: ${err.message}`);
        this.operations.set([]);
        this.loading.set(false);
      }
    });
  }

  depot(valeur: string) {
    const mnt = parseFloat(valeur);
    const num = this.compte()?.numeroCompte;

    if (num && mnt > 0) {
      const operationRequest = {
        numeroCompte: num,
        montant: mnt,
        libelle: 'Dépôt via Dashboard'
      };

      this.bankService.depot(operationRequest).subscribe({
        next: () => {
          this.rechercher(num);
          this.operationErreur.set("");
        },
        error: (err: ErrorResponse) => {
          console.error(`Code ${err.status}: ${err.message}`);
          this.operationErreur.set(err.message);
        }
      });
    }
  }

  retrait(valeur: string) {
    const mnt = parseFloat(valeur);
    const num = this.compte()?.numeroCompte;

    if (num && mnt > 0) {
      const operationRequest = {
        numeroCompte: num,
        montant: mnt,
        libelle: 'Retrait via Dashboard'
      };

      this.bankService.retrait(operationRequest).subscribe({
        next: () => {
          this.rechercher(num);
          this.operationErreur.set("");
        },
        error: (err: ErrorResponse) => {
          console.error(`Code ${err.status}: ${err.message}`);
          this.operationErreur.set(err.message);
        }
      });
    }
  }
}
