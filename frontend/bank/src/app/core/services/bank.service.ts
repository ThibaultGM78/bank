import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Compte } from '../../shared/models/compte.model';
import { Operation } from '../../shared/models/operation.model';
import { ErrorResponse } from '../../shared/models/error-response.model';

@Injectable({
  providedIn: 'root'
})
export class BankService {
  private http = inject(HttpClient);
  
  private readonly API_URL = 'http://localhost:8081/bank';

  getCompte(numeroCompte: string): Observable<Compte> {
    return this.http.get<Compte>(`${this.API_URL}/compte/${numeroCompte}`).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorBody: ErrorResponse = error.error;
        return throwError(() => errorBody);
      })
    );;
  }

  getReleve(numeroCompte: string): Observable<Operation[]> {
    return this.http.get<Operation[]>(`${this.API_URL}/compte/${numeroCompte}/releve`).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorBody: ErrorResponse = error.error;
        return throwError(() => errorBody);
      })
    );;
  }

  depot(operation: Partial<Operation>): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/operation/depot`, operation).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorBody: ErrorResponse = error.error;
        return throwError(() => errorBody);
      })
    );;
  }

  retrait(operation: Partial<Operation>): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/operation/retrait`, operation).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorBody: ErrorResponse = error.error;
        return throwError(() => errorBody);
      })
    );
  }

}