package pl.gkawalec.pgk.application.legalact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;

import java.time.LocalDate;
import java.util.UUID;

@Component
class TestLegalActCreator {

    @Autowired
    private LegalActRepository legalActRepository;

    LegalActEntity createForLink(String link) {
        LegalActEntity entity = createTemplate();
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.link, link);
        return legalActRepository.save(entity);
    }

    LegalActEntity createForNameAndDateOf(String name, LocalDate dateOf) {
        LegalActEntity entity = createTemplate();
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.name, name);
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.dateOf, dateOf);
        return legalActRepository.save(entity);
    }

    static LegalActEntity createTemplate() {
        LegalActEntity entity = new LegalActEntity();
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.name, UUID.randomUUID().toString());
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.dateOf, LocalDate.now());
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.link, "www." + UUID.randomUUID() + ".pl");
        ReflectionTestUtils.setField(entity, LegalActDTO.Fields.description, UUID.randomUUID().toString());
        return entity;
    }


}
