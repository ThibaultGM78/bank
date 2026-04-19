import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BankService } from './bank.service';
import { ErrorResponse } from '../../shared/models/error-response.model';
import { Operation } from '../../shared/models/operation.model';

describe('BankService', () => {
  let service: BankService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BankService]
    });
    service = TestBed.inject(BankService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getCompte', () => {
    it('should return a Compte via GET request', () => {
      const mockCompte = { numeroCompte: 'CC1', solde: 1000 };

      service.getCompte('CC1').subscribe((data) => {
        expect(data).toEqual(mockCompte as any);
      });

      const req = httpMock.expectOne(req => req.url.endsWith('/compte/CC1'));
      expect(req.request.method).toBe('GET');
      req.flush(mockCompte);
    });

    it('should handle 404 error using ErrorResponse', () => {
      const mockError: ErrorResponse = { status: 404, message: 'Compte introuvable' };

      service.getCompte('INCONNU').subscribe({
        error: (err) => expect(err).toEqual(mockError)
      });

      const req = httpMock.expectOne(req => req.url.endsWith('/compte/INCONNU'));
      req.flush(mockError, { status: 404, statusText: 'Not Found' });
    });
  });

  describe('Operations (Depot / Retrait)', () => {
    it('should send a POST request for depot', () => {
      const op: Partial<Operation> = { numeroCompte: 'CC1', montant: 50 };

      service.depot(op).subscribe();

      const req = httpMock.expectOne(req => req.url.endsWith('/operation/depot'));
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(op);
      req.flush(null); 
    });

    it('should handle business error for retrait', () => {
      const mockError: ErrorResponse = { status: 400, message: 'Solde insuffisant' };
      const op: Partial<Operation> = { numeroCompte: 'CC1', montant: 5000 };

      service.retrait(op).subscribe({
        error: (err) => {
          expect(err.status).toBe(400);
          expect(err.message).toBe('Solde insuffisant');
        }
      });

      const req = httpMock.expectOne(req => req.url.endsWith('/operation/retrait'));
      req.flush(mockError, { status: 400, statusText: 'Bad Request' });
    });
  });
});