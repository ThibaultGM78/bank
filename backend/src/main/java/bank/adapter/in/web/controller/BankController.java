package bank.adapter.in.web.controller;

import bank.adapter.in.web.dto.CompteDTO;
import bank.adapter.in.web.dto.VirementDTO;
import bank.adapter.in.web.helper.BankHelper;
import bank.application.domain.Compte;
import bank.application.domain.Virement;
import bank.application.usecase.IExecuterVirementUseCase;
import bank.application.usecase.IGetCompteUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankController {

    private final IGetCompteUseCase getCompteUseCase;
    private final IExecuterVirementUseCase executerVirementUseCase;

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World !");
    }

    @GetMapping("/compte/{n}"
    //produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CompteDTO> getCompte(@PathVariable("n") String n) {
        try {
            Compte compte = getCompteUseCase.getCompte(n);

            CompteDTO compteDto = BankHelper.toDTO(compte);

            return ResponseEntity.ok(compteDto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
    @PostMapping(value ="/virement",
            consumes = APPLICATION_JSON_VALUE
            //produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> executerVirement(@RequestBody @NonNull VirementDTO virementDTO) {

        try {
            Virement virement = BankHelper.from(virementDTO);
            executerVirementUseCase.executeTransfer(virement);

            return ResponseEntity.status(HttpStatus.CREATED).body("Le virement a été enregistré avec succès.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("Erreur métier : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur technique est survenue lors du virement.");
        }
    }
    */
}
