package bank.application.usecase.impl;

import bank.application.dao.IOperationDAO;
import bank.application.domain.Operation;
import bank.application.usecase.IGetOperationsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class GetOperationsUseCase implements IGetOperationsUseCase {

    private final IOperationDAO operationDAO;

    public List<Operation> getOperations(String numeroCompte) {
        requireNonNull(numeroCompte);
        return operationDAO.getOperations(numeroCompte);
    }
}
