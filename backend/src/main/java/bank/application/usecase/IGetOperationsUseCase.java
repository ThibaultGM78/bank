package bank.application.usecase;

import bank.application.domain.Operation;

import java.util.List;

public interface IGetOperationsUseCase {
    List<Operation> getOperations(String numeroCompte);
}
