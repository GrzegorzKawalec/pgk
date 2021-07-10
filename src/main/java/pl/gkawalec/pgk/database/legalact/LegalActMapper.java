package pl.gkawalec.pgk.database.legalact;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;

@UtilityClass
public class LegalActMapper {

    public LegalActEntity create(LegalActDTO dto) {
        LegalActEntity entity = new LegalActEntity();
        changeField(dto, entity);
        return entity;
    }

    public void update(LegalActDTO dto, LegalActEntity entity) {
        changeField(dto, entity);
        entity.setVersion(dto.getEntityVersion());
    }

    private void changeField(LegalActDTO dto, LegalActEntity entity) {
        entity.setName(dto.getName());
        entity.setDateOf(dto.getDateOf());
        entity.setLink(dto.getLink());
        entity.setDescription(dto.getDescription());
    }

}
