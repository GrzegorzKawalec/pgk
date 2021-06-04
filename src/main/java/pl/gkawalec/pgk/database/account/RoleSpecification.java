package pl.gkawalec.pgk.database.account;

import org.springframework.data.jpa.domain.Specification;
import pl.gkawalec.pgk.api.dto.account.RoleCriteria;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.PredicateUtil;
import pl.gkawalec.pgk.common.util.StringUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public record RoleSpecification(RoleCriteria criteria) implements Specification<RoleEntity> {

    @Override
    @ParametersAreNonnullByDefault
    public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        addSearchBy(root, criteriaBuilder, predicates);
        addAuthorities(root, predicates);

        criteriaQuery.distinct(true);
        return PredicateUtil.togetherAnd(predicates, criteriaBuilder);
    }

    private void addSearchBy(Root<RoleEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (StringUtil.isBlank(criteria.getSearchBy())) {
            return;
        }
        Path<String> namePath = root.get(RoleEntity_.name);
        Predicate like = PredicateUtil.like(criteria.getSearchBy(), namePath, criteriaBuilder);
        predicates.add(like);
    }

    private void addAuthorities(Root<RoleEntity> root, List<Predicate> predicates) {
        if (CollectionUtil.isEmpty(criteria.getAuthorities())) {
            return;
        }
        ListJoin<RoleEntity, AuthorityEntity> join = root.join(RoleEntity_.authorities);
        Path<Authority> authorityPath = join.get(AuthorityEntity_.authority);
        Predicate in = authorityPath.in(criteria.getAuthorities());
        predicates.add(in);
    }

}
