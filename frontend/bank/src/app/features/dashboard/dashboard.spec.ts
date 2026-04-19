import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Dashboard } from './dashboard';
import { BankService } from '../../core/services/bank.service';
import { of, throwError } from 'rxjs';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { ErrorResponse } from '../../shared/models/error-response.model';

describe('Dashboard Component', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;
  let bankServiceMock: any;

  beforeEach(async () => {
    bankServiceMock = {
      getCompte: vi.fn(),
      getReleve: vi.fn(),
      depot: vi.fn(),
      retrait: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        { provide: BankService, useValue: bankServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  describe('rechercher()', () => {
    it('should update compte and operations signals on success', () => {
      const mockCompte = { 
        numeroCompte: 'CC-123', 
        solde: 500, 
        detailsCompteCourant: { montantDecouvert: 100 } 
      };
      const mockOps = [{ id: 1, montant: 50, libelle: 'Test', type: 'CREDIT' }];
      
      bankServiceMock.getCompte.mockReturnValue(of(mockCompte));
      bankServiceMock.getReleve.mockReturnValue(of(mockOps));

      component.rechercher('CC-123');

      expect(component.compte()).toEqual(mockCompte as any);
      expect(component.operations()).toEqual(mockOps as any);
      expect(component.typeCompte()).toBe('Compte Courant');
    });

    it('should set compte to null and operations to empty on error', () => {
      const error: ErrorResponse = { status: 404, message: 'Compte introuvable' };
      bankServiceMock.getCompte.mockReturnValue(throwError(() => error));
      bankServiceMock.getReleve.mockReturnValue(of([]));

      component.rechercher('INCONNU');

      expect(component.compte()).toBeNull();
      expect(component.operations()).toEqual([]);
    });
  });

  describe('depot()', () => {
    it('should call depot and refresh data on success', () => {
      component.compte.set({ numeroCompte: 'CC1', solde: 100 } as any);
      bankServiceMock.depot.mockReturnValue(of(undefined));
      
      bankServiceMock.getCompte.mockReturnValue(of({ numeroCompte: 'CC1', solde: 150 }));
      bankServiceMock.getReleve.mockReturnValue(of([]));

      component.depot('50');

      expect(bankServiceMock.depot).toHaveBeenCalledWith(expect.objectContaining({
        numeroCompte: 'CC1',
        montant: 50
      }));
      expect(component.operationErreur()).toBe('');
    });

    it('should set operationErreur signal when depot fails', () => {
      component.compte.set({ numeroCompte: 'CC1', solde: 100 } as any);
      const error: ErrorResponse = { status: 400, message: 'Montant invalide' };
      bankServiceMock.depot.mockReturnValue(throwError(() => error));

      component.depot('50');

      expect(component.operationErreur()).toBe('Montant invalide');
    });
  });

  describe('retrait()', () => {
    it('should set error message when withdrawal is refused', () => {
      component.compte.set({ numeroCompte: 'CC1', solde: 10 } as any);
      const error: ErrorResponse = { status: 400, message: 'Solde insuffisant' };
      bankServiceMock.retrait.mockReturnValue(throwError(() => error));

      component.retrait('100');

      expect(component.operationErreur()).toBe('Solde insuffisant');
      expect(bankServiceMock.retrait).toHaveBeenCalled();
    });
  });
});