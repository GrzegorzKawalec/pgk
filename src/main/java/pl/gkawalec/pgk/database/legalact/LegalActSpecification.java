package pl.gkawalec.pgk.database.legalact;

import org.springframework.data.jpa.domain.Specification;
import pl.gkawalec.pgk.api.dto.legalact.LegalActCriteria;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.common.util.PredicateUtil;
import pl.gkawalec.pgk.common.util.StringUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record LegalActSpecification(LegalActCriteria criteria) implements Specification<LegalActEntity> {

    @Override
    @ParametersAreNonnullByDefault
    public Predicate toPredicate(Root<LegalActEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesOr = new ArrayList<>();

        isActive(root, criteriaBuilder, predicates);
        addBoundaryDates(root, criteriaBuilder, predicates);
        addSearchBy(root, criteriaBuilder, predicatesOr);

        if (CollectionUtil.isNotEmpty(predicatesOr)) {
            Predicate or = PredicateUtil.togetherOr(predicatesOr, criteriaBuilder);
            predicates.add(or);
        }

        criteriaQuery.distinct(true);
        return PredicateUtil.togetherAnd(predicates, criteriaBuilder);
    }

    private void isActive(Root<LegalActEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        Path<Boolean> isActivePath = root.get(LegalActEntity_.isActive);
        Predicate isActivePredicate = criteria.getIsActive() ?
                criteriaBuilder.isTrue(isActivePath) :
                criteriaBuilder.isFalse(isActivePath);
        predicates.add(isActivePredicate);
    }

    private void addBoundaryDates(Root<LegalActEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        Path<LocalDate> dateOfPath = root.get(LegalActEntity_.dateOf);
        if (Objects.nonNull(criteria.getDateOfGreaterThanOrEqual())) {
            LocalDate criteriaDate = DateTimeUtil.stringToLocalDate(criteria.getDateOfGreaterThanOrEqual());
            Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(dateOfPath, criteriaDate);
            predicates.add(datePredicate);
        }
        if (Objects.nonNull(criteria.getDateOfLessThanOrEqual())) {
            LocalDate criteriaDate = DateTimeUtil.stringToLocalDate(criteria.getDateOfLessThanOrEqual());
            Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(dateOfPath, criteriaDate);
            predicates.add(datePredicate);
        }
    }

    private void addSearchBy(Root<LegalActEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (StringUtil.isBlank(criteria.getSearchBy())) {
            return;
        }
        predicates.add(searchLikeBy(root, criteriaBuilder, LegalActEntity_.name));
        predicates.add(searchLikeBy(root, criteriaBuilder, LegalActEntity_.description));
    }

    private Predicate searchLikeBy(Root<LegalActEntity> root, CriteriaBuilder criteriaBuilder, SingularAttribute<LegalActEntity, String> column) {
        Path<String> path = root.get(column);
        return PredicateUtil.like(criteria.getSearchBy(), path, criteriaBuilder);
    }

}
