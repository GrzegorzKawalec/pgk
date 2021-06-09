package pl.gkawalec.pgk.database.audit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Entity
@Setter(AccessLevel.PACKAGE)
@Table(name = "audit_request")
public class AuditRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    private String reqUri;

    private String reqMethod;

    private String reqParam;

    private String reqBody;

    private String response;

    @Size(max = 36)
    private String errorUuid;

    @Enumerated(EnumType.STRING)
    private ResponseExceptionType errorType;

    private String errorMessage;

    private Long processDurationMillis;

    private Long auditDurationMillis;

}
