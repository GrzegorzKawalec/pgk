package pl.gkawalec.pgk.database.project;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import pl.gkawalec.pgk.api.dto.project.ProjectCriteria;
import pl.gkawalec.pgk.common.util.PredicateUtil;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity_;
import pl.gkawalec.pgk.database.legalact.LegalActEntity_;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ProjectSpecification(ProjectCriteria criteria) implements Specification<ProjectEntity> {

    @Override
    @ParametersAreNonnullByDefault
    public Predicate toPredicate(Root<ProjectEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        isActive(root, criteriaBuilder, predicates);
        addSearchBy(root, criteriaBuilder, predicates);
        addContainLegalAct(root, criteriaBuilder, predicates);
        addContainParticipant(root, criteriaBuilder, predicates);

        criteriaQuery.distinct(true);
        setOderBy(root, criteriaQuery, criteriaBuilder);
        return PredicateUtil.togetherAnd(predicates, criteriaBuilder);
    }

    private void isActive(Root<ProjectEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        Path<Boolean> isActivePath = root.get(ProjectEntity_.isActive);
        Predicate isActivePredicate = criteria.getIsActive() ?
                criteriaBuilder.isTrue(isActivePath) :
                criteriaBuilder.isFalse(isActivePath);
        predicates.add(isActivePredicate);
    }

    private void addSearchBy(Root<ProjectEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (StringUtil.isBlank(criteria.getSearchBy())) {
            return;
        }
        predicates.add(searchLikeBy(root, criteriaBuilder, ProjectEntity_.name));
        predicates.add(searchLikeBy(root, criteriaBuilder, ProjectEntity_.description));
    }

    private Predicate searchLikeBy(Root<ProjectEntity> root, CriteriaBuilder criteriaBuilder, SingularAttribute<ProjectEntity, String> column) {
        Path<String> path = root.get(column);
        return PredicateUtil.like(criteria.getSearchBy(), path, criteriaBuilder);
    }

    private void addContainLegalAct(Root<ProjectEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (Objects.isNull(criteria.getLegalActId())) {
            return;
        }
        Path<Long> legalActIdPath = root.join(ProjectEntity_.legalActs)
                .get(LegalActEntity_.id);
        Predicate containLegalActIdPredicate = criteriaBuilder.equal(legalActIdPath, criteria.getLegalActId());
        predicates.add(containLegalActIdPredicate);
    }

    private void addContainParticipant(Root<ProjectEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (Objects.isNull(criteria.getParticipantId())) {
            return;
        }
        Path<Integer> participantIdPath = root.join(ProjectEntity_.participants)
                .get(UserEntity_.id);
        Predicate containParticipantIdPredicate = criteriaBuilder.equal(participantIdPath, criteria.getParticipantId());
        predicates.add(containParticipantIdPredicate);
    }

    private void setOderBy(Root<ProjectEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (Objects.isNull(criteria.getOrderBy()) || Objects.isNull(criteria.getOrderDirection())) {
            return;
        }
        Path<?> orderPath = switch (criteria.getOrderBy()) {
            case name -> orderByName(root);
            case dateStart -> orderByDateStart(root);
            case dateEnd -> orderByDateEnd(root);
            case projectManager -> orderByProjectManager(root);
        };
        Order order = Sort.Direction.ASC.equals(criteria.getOrderDirection()) ?
                criteriaBuilder.asc(orderPath) : criteriaBuilder.desc(orderPath);
        criteriaQuery.orderBy(order);
    }

    private Path<?> orderByName(Root<ProjectEntity> root) {
        return root.get(ProjectEntity_.name);
    }

    private Path<?> orderByDateStart(Root<ProjectEntity> root) {
        return root.get(ProjectEntity_.dateStart);
    }

    private Path<?> orderByDateEnd(Root<ProjectEntity> root) {
        return root.get(ProjectEntity_.dateEnd);
    }

    private Path<?> orderByProjectManager(Root<ProjectEntity> root) {
        return root.join(ProjectEntity_.projectManager)
                .get(UserEntity_.lastName);
    }

}
