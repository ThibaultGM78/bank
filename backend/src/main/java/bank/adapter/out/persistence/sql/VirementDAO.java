package bank.adapter.out.persistence.sql;

import bank.application.dao.IVirementDAO;
import bank.application.domain.Virement;
import org.springframework.stereotype.Repository;

@Repository
public class VirementDAO implements IVirementDAO {
    @Override
    public void sauvegarder(Virement virement) {

    }
}
