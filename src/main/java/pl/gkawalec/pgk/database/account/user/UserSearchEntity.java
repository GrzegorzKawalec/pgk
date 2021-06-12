package pl.gkawalec.pgk.database.account.user;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@Entity
@Immutable
@Table(name = "pgk_user")
public class UserSearchEntity {

    @Id
    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(
            name = "email",
            referencedColumnName = "email",
            insertable = false,
            updatable = false
    )
    private UserCredentialsEntity credentials;

}
