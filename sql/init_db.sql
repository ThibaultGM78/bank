CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

DROP TABLE IF EXISTS BANK_OPERATION;
DROP TABLE IF EXISTS BANK_COMPTE;

CREATE TABLE BANK_COMPTE (
    id_compte VARCHAR(50) PRIMARY KEY,
    compte_type ENUM('COURANT', 'LIVRET') NOT NULL,
    compte_solde DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    compte_decouvert_autorise BOOLEAN DEFAULT FALSE,
    compte_montant_decouvert DECIMAL(15, 2) DEFAULT 0.00,
    compte_plafond DECIMAL(15, 2) DEFAULT NULL
);

CREATE TABLE BANK_OPERATION (
    id_operation BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_compte VARCHAR(50) NOT NULL,
    operation_type VARCHAR(20) NOT NULL,
    operation_montant DECIMAL(15, 2) NOT NULL,
    operation_date TIMESTAMP NOT NULL,
    operation_solde_apres DECIMAL(15, 2) NOT NULL, 
    
    id_compte_distant VARCHAR(50), 
    
    operation_libelle VARCHAR(255),
    CONSTRAINT fk_compte FOREIGN KEY (id_compte) REFERENCES BANK_COMPTE(id_compte)
);

INSERT INTO BANK_COMPTE (id_compte, compte_type, compte_solde, compte_decouvert_autorise, compte_montant_decouvert) 
VALUES 
('CC-JACK-001', 'COURANT', 2500.00, TRUE, 500.00),
('CC-BOB-002', 'COURANT', 10.00, FALSE, 0.00);

INSERT INTO BANK_COMPTE (id_compte, compte_type, compte_solde, compte_plafond) 
VALUES 
('LA-ALICE-003', 'LIVRET', 15000.00, 22950.00),
('LA-PICSOU-004', 'LIVRET', 22950.00, 22950.00);

INSERT INTO BANK_OPERATION (id_operation, id_compte, operation_type, operation_montant, operation_date, operation_solde_apres, operation_libelle) 
VALUES 
(1, 'CC-JACK-001', 'DEPOT', 500.00, CURRENT_TIMESTAMP, 3000.00, 'Dépôt espèces agence');