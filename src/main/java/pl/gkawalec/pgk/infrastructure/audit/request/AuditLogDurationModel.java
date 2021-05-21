package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

public class AuditLogDurationModel {

    private final Instant startAudit;
    private final Instant startProcess;
    private final Instant endProcess;

    @Getter
    private long durationAuditMillis;
    @Getter
    private long durationProcessMillis;

    AuditLogDurationModel(Instant startAudit, Instant startProcess, Instant endProcess) {
        this.startAudit = startAudit;
        this.startProcess = startProcess;
        this.endProcess = endProcess;
    }

    void calculateDuration() {
        Duration durationProcess = Duration.between(startProcess, endProcess);
        this.durationProcessMillis = durationProcess.toMillis();
        Duration durationAudit = Duration.between(startAudit, Instant.now());
        this.durationAuditMillis = durationAudit.toMillis();
    }

}
