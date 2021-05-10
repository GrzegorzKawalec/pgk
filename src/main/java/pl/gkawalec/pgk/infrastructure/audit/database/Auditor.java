package pl.gkawalec.pgk.infrastructure.audit.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Auditor {

    private Integer userId;
    private LocalDateTime date;

}
