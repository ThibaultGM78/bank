package bank.adapter.in.web.dto;

import bank.adapter.in.web.dto.compte.DetailsCompteCourantDTO;
import bank.adapter.in.web.dto.compte.DetailsLivretDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@Builder
@JsonInclude(NON_NULL)
public class CompteDTO {
    String numeroCompte;
    BigDecimal solde;
    DetailsCompteCourantDTO detailsCompteCourant;
    DetailsLivretDTO detailsLivret;
}
