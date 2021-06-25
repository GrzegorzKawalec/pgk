package pl.gkawalec.pgk.api.dto.common.auditing;

public interface AuditingDTO<T> {

    T getDto();
    AuditingInfoDTO getInfo();

}
