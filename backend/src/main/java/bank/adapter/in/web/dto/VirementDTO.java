package bank.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@Builder
@JsonInclude(NON_NULL)
public class VirementDTO {

    String numeroCompteEmetteur;

    String numeroCompteRecepteur;

    BigDecimal montant;

    String libelle;

    LocalDate date;
}
