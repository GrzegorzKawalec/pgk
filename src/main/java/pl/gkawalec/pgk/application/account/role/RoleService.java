package pl.gkawalec.pgk.application.account.role;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.account.role.RoleAuditingDTO;
import pl.gkawalec.pgk.api.dto.account.role.RoleCriteria;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingDTO;
import pl.gkawalec.pgk.common.auditing.AuditingMapper;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntityMapper;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.role.RoleSpecification;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleValidator validator;
    private final RoleUniqueNameChecker uniqueNameChecker;

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    private final AuditingMapper auditingMapper;

    public boolean existsName(String roleName, Integer roleId) {
        return uniqueNameChecker.existsTrimName(roleName, roleId);
    }

    public RoleDTO findById(Integer roleId) {
        RoleEntity roleEntity = findEntityById(roleId);
        return RoleDTO.of(roleEntity);
    }

    @Transactional
    public RoleDTO create(RoleDTO dto) {
        validator.validateCreate(dto);
        List<AuthorityEntity> authorityEntities = authorityRepository.findAllByAuthorityIn(dto.getAuthorities());
        RoleEntity roleEntity = RoleEntityMapper.create(dto.getName(), dto.getDescription(), authorityEntities);
        roleEntity = roleRepository.save(roleEntity);
        return RoleDTO.of(roleEntity);
    }

    @Transactional
    public RoleDTO update(RoleDTO dto) {
        validator.validateUpdate(dto);
        List<AuthorityEntity> authorityEntities = authorityRepository.findAllByAuthorityIn(dto.getAuthorities());
        RoleEntity roleEntity = roleRepository.findOneById(dto.getId());
        RoleEntityMapper.update(dto, roleEntity, authorityEntities);
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        return RoleDTO.of(roleEntity);
    }

    @Transactional
    public void delete(Integer roleId) {
        validator.validateDelete(roleId);
        RoleEntity roleEntity = roleRepository.findOneById(roleId);
        roleRepository.delete(roleEntity);
    }

    public Page<RoleDTO> find(RoleCriteria criteria) {
        criteria = Objects.nonNull(criteria) ? criteria : new RoleCriteria();
        RoleSpecification specification = new RoleSpecification(criteria);
        PageRequest pageRequest = criteria.getSearchPage().toPageRequest();
        return roleRepository.findAll(specification, pageRequest)
                .map(RoleDTO::of);
    }

    public RoleAuditingDTO getAuditingInfo(Integer roleId) {
        RoleEntity roleEntity = findEntityById(roleId);
        AuditingDTO auditingDTO = auditingMapper.map(roleEntity);
        return RoleAuditingDTO.of(roleEntity, auditingDTO);
    }

    private RoleEntity findEntityById(Integer roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseException(ResponseExceptionType.ROLE_NOT_FOUND));
    }

}
