package bank.application.usecase;

import bank.adapter.in.web.dto.VirementDTO;
import bank.application.domain.Virement;

public interface IExecuterVirementUseCase {
    void executeTransfer(Virement virement);
}
