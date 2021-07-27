package pl.gkawalec.pgk.database.project;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

import java.util.List;

public interface ProjectRepository extends BaseNumberIDRepository<ProjectEntity> {

    List<ProjectEntity> findAllByActiveIsTrueAndParticipants_id(Integer participantId);

}
