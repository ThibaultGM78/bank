DROP TABLE IF EXISTS BANK_VIREMENT;
DROP TABLE IF EXISTS BANK_COMPTE;

CREATE TABLE BANK_COMPTE (
    id_compte VARCHAR(50) PRIMARY KEY,
    compte_type ENUM('COURANT', 'LIVRET') NOT NULL,
    compte_solde DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    compte_decouvert_autorise BOOLEAN DEFAULT FALSE,
    compte_montant_decouvert DECIMAL(15, 2) DEFAULT 0.00,
    compte_plafond DECIMAL(15, 2) DEFAULT NULL
);

CREATE TABLE BANK_VIREMENT (
    id_virement INT AUTO_INCREMENT PRIMARY KEY,
    id_compte_emetteur VARCHAR(50) NOT NULL,
    id_compte_recepteur VARCHAR(50) NOT NULL,
    virement_montant DECIMAL(15, 2) NOT NULL,
    virement_libelle VARCHAR(255),
    virement_date DATE NOT NULL,
    CONSTRAINT fk_emetteur FOREIGN KEY (id_compte_emetteur) REFERENCES BANK_COMPTE(id_compte),
    CONSTRAINT fk_recepteur FOREIGN KEY (id_compte_recepteur) REFERENCES BANK_COMPTE(id_compte)
);

INSERT INTO BANK_COMPTE (id_compte, compte_type, compte_solde, compte_decouvert_autorise, compte_montant_decouvert) 
VALUES 
('CC-JACK-001', 'COURANT', 2500.00, TRUE, 500.00),
('CC-BOB-002', 'COURANT', 10.00, FALSE, 0.00);

INSERT INTO BANK_COMPTE (id_compte, compte_type, compte_solde, compte_plafond) 
VALUES 
('LA-ALICE-003', 'LIVRET', 15000.00, 22950.00),
('LA-PICSOU-004', 'LIVRET', 22950.00, 22950.00);

INSERT INTO BANK_VIREMENT (id_compte_emetteur, id_compte_recepteur, virement_montant, virement_libelle, virement_date)
VALUES ('CC-JACK-001', 'LA-ALICE-003', 100.00, 'Cadeau anniversaire', CURDATE());