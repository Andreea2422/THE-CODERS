package ro.ubbcluj.map.thecoders.repository.paging;


import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.repository.Repository;

public interface PagingRepository<ID ,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
}
