package bank.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class OperationDTO {
    Long id;
    String type;
    String numeroCompte;
    String numeroCompteDistant;
    BigDecimal montant;
    BigDecimal soldeApres;
    String libelle;
    LocalDateTime date;
}
