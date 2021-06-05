package pl.gkawalec.pgk.database.account.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Entity
@Audited
@Table(name = "pgk_user")
@Setter(AccessLevel.PACKAGE)
public class UserEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @NotNull
    @Size(max = 50)
    private String lastName;

    @Size(max = 20)
    private String phoneNumber;

    private String description;

    @NotNull
    private boolean isActive = true;

}
