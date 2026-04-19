export interface Operation {
  id: number;
  type: string;
  numeroCompte: string;
  numeroCompteDistant?: string;
  montant: number;
  soldeApres: number;
  libelle: string;
  date: string | Date;
}