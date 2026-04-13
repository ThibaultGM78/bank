package bank.application.dao;

import bank.application.domain.Operation;

import java.util.List;

public interface IOperationDAO {

    void save(Operation operation);
    List<Operation> getOperations(String numeroCompte);
}
