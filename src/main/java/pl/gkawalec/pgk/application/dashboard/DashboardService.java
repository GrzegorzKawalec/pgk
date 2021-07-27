package pl.gkawalec.pgk.application.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.dashboard.DashboardDTO;
import pl.gkawalec.pgk.common.user.LoggedUserAccessor;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.database.project.ProjectRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final LoggedUserAccessor loggedUserAccessor;

    private final ProjectRepository projectRepository;

    @Transactional
    public DashboardDTO getDashboard() {
        Integer loggedUserId = loggedUserAccessor.getUserId();
        List<ProjectEntity> projectEntities = projectRepository.findAllByActiveIsTrueAndParticipants_id(loggedUserId);
        return DashboardDTO.of(projectEntities);
    }

}
