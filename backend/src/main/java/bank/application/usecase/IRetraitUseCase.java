package bank.application.usecase;

import bank.application.domain.Operation;

public interface IRetraitUseCase {
    void retrait(Operation operation);
}
