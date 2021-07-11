package pl.gkawalec.pgk.application.legalact;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActAuditingDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActCriteria;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.common.auditing.AuditingMapper;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.legalact.LegalActMapper;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;
import pl.gkawalec.pgk.database.legalact.LegalActSpecification;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LegalActService {

    private final LegalActValidator validator;

    private final AuditingMapper auditingMapper;

    private final LegalActRepository legalActRepository;

    public LegalActDTO findById(Long legalActId) {
        LegalActEntity legalActEntity = findEntityById(legalActId);
        return LegalActDTO.of(legalActEntity);
    }

    @Transactional
    public LegalActDTO create(LegalActDTO dto) {
        validator.validateCreate(dto);
        LegalActEntity legalActEntity = LegalActMapper.create(dto);
        legalActEntity = legalActRepository.save(legalActEntity);
        return LegalActDTO.of(legalActEntity);
    }

    @Transactional
    public LegalActDTO update(LegalActDTO dto) {
        validator.validateUpdate(dto);
        LegalActEntity legalActEntity = legalActRepository.findOneById(dto.getId());
        LegalActMapper.update(dto, legalActEntity);
        legalActEntity = legalActRepository.saveAndFlush(legalActEntity);
        return LegalActDTO.of(legalActEntity);
    }

    @Transactional
    public void deactivate(Long legalActId) {
        LegalActEntity legalActEntity = findEntityById(legalActId);
        legalActEntity.deactivate();
    }

    @Transactional
    public void activate(Long legalActId) {
        LegalActEntity legalActEntity = findEntityById(legalActId);
        legalActEntity.activate();
    }

    public Page<LegalActDTO> find(LegalActCriteria criteria) {
        criteria = Objects.nonNull(criteria) ? criteria : new LegalActCriteria();
        LegalActSpecification specification = new LegalActSpecification(criteria);
        PageRequest pageRequest = criteria.getSearchPage().toPageRequest();
        return legalActRepository.findAll(specification, pageRequest)
                .map(LegalActDTO::of);
    }

    public LegalActAuditingDTO getAuditingInfo(Long legalActId) {
        LegalActEntity legalActEntity = findEntityById(legalActId);
        AuditingInfoDTO auditingInfoDTO = auditingMapper.map(legalActEntity);
        return LegalActAuditingDTO.of(legalActEntity, auditingInfoDTO);
    }

    private LegalActEntity findEntityById(Long legalActId) {
        return legalActRepository.findById(legalActId)
                .orElseThrow(() -> new ResponseException(ResponseExceptionType.LEGAL_ACT_NOT_FOUND));
    }

}
