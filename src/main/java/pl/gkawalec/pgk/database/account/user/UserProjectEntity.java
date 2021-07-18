package pl.gkawalec.pgk.database.account.user;

import lombok.Getter;
import org.hibernate.annotations.Immutable;
import pl.gkawalec.pgk.database.project.ProjectEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Entity
@Immutable
@Table(name = "pgk_user")
public class UserProjectEntity extends BaseUserEntity {

    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
    private List<ProjectEntity> projects;

}
