package bank.adapter.in.web.controller;

import bank.adapter.in.web.dto.OperationDTO;
import bank.adapter.in.web.helper.BankHelper;
import bank.application.domain.Compte;
import bank.application.domain.Operation;
import bank.application.usecase.IDepotUseCase;
import bank.application.usecase.IGetCompteUseCase;
import bank.application.usecase.IGetOperationsUseCase;
import bank.application.usecase.IRetraitUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
@CrossOrigin("http://localhost:4200")
public class BankController {

    public static final String ERR_UNEXPECTED = "Une erreur inattendue est survenue";
    public static final String ERR_DEPOT_PREFIX = "Erreur lors du dépôt : ";
    public static final String ERR_RETRAIT_PREFIX = "Erreur lors du retrait : ";

    private final IGetCompteUseCase getCompteUseCase;
    private final IDepotUseCase depotUseCase;
    private final IRetraitUseCase retraitUseCase;
    private final IGetOperationsUseCase getOperationsUseCase;

    public record ErrorResponse(int status, String message) {}

    @GetMapping("/compte/{n}")
    public ResponseEntity<?> getCompte(@PathVariable String n) {
        try {
            Compte compte = getCompteUseCase.getCompte(n);
            return ResponseEntity.ok(BankHelper.toDTO(compte));

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, ERR_UNEXPECTED));
        }
    }

    @PostMapping("operation/depot")
    public ResponseEntity<?> deposer(@RequestBody OperationDTO operationDTO) {
        try {
            Operation operation = BankHelper.toDomain(operationDTO);

            depotUseCase.depot(operation);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500,  ERR_DEPOT_PREFIX + e.getMessage()));
        }
    }

    @PostMapping("operation/retrait")
    public ResponseEntity<?> retirer(@RequestBody OperationDTO operationDTO) {
        try {
            Operation operation = BankHelper.toDomain(operationDTO);

            retraitUseCase.retrait(operation);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, ERR_RETRAIT_PREFIX + e.getMessage()));
        }
    }

    @GetMapping("/compte/{n}/releve")
    public ResponseEntity<?> getOperations(@PathVariable String n) {
        try {
            List<Operation> operations = getOperationsUseCase.getOperations(n);
            return ResponseEntity.ok(operations.stream()
                    .map(BankHelper::toDTO)
                    .toList());

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, ERR_UNEXPECTED));
        }
    }
}
