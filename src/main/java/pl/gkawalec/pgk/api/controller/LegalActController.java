package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.gkawalec.pgk.api.dto.legalact.LegalActCriteria;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.application.legalact.LegalActService;
import pl.gkawalec.pgk.common.annotation.request.AuditedRequest;
import pl.gkawalec.pgk.common.annotation.security.AuthGuard;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + LegalActController.URL)
public class LegalActController {

    public static final String URL = "/legal-act";
    public static final String FIND = "/find";

    private final LegalActService legalActService;

    @GetMapping("{id}")
    @AuthGuard({Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE})
    public LegalActDTO findById(@PathVariable("id") Long legalActId) {
        return legalActService.findById(legalActId);
    }

    @PostMapping
    @AuthGuard(Authority.LEGAL_ACTS_WRITE)
    public LegalActDTO create(@RequestBody LegalActDTO dto) {
        return legalActService.create(dto);
    }

    @PutMapping
    @AuthGuard(Authority.LEGAL_ACTS_WRITE)
    public LegalActDTO update(@RequestBody LegalActDTO dto) {
        return legalActService.update(dto);
    }

    @PostMapping(FIND)
    @AuditedRequest(false)
    public Page<LegalActDTO> find(@RequestBody(required = false) LegalActCriteria criteria) {
        return legalActService.find(criteria);
    }

}
