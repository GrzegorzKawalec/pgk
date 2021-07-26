package pl.gkawalec.pgk.application.legalact;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;

@UtilityClass
class TestLegalActDTOPreparer {

    LegalActDTO prepareNew() {
        LegalActEntity entity = TestLegalActCreator.createTemplate();
        return prepare(entity);
    }

    LegalActDTO prepare(LegalActEntity entity) {
        return LegalActDTO.of(entity);
    }

}
