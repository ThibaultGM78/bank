import {DetailsCompteCourant} from "./details-compte-courant.model"
import {DetailsLivret} from "./details-livret.model"

export interface Compte {
  numeroCompte: string;
  solde: number;
  detailsCompteCourant?: DetailsCompteCourant;
  detailsLivret?: DetailsLivret;
}