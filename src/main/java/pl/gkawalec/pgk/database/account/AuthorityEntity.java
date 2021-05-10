package pl.gkawalec.pgk.database.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pgk_authority")
public class AuthorityEntity extends AuditingEntity {

    @Id
    private Short id;

    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Authority authority;

}
