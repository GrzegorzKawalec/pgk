package pl.gkawalec.pgk.common.jpa;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseNumberIDRepository<T> extends BaseRepository<T, Number> {
}
