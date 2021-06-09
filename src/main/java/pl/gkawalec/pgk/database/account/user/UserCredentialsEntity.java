package pl.gkawalec.pgk.database.account.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Audited
@Setter(AccessLevel.PACKAGE)
@Table(name = "pgk_user_credentials")
public class UserCredentialsEntity extends AuditingEntity {

    @Id
    private String email;

    @NotNull
    @NotAudited
    private String password;

    @NotNull
    @OneToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

}
