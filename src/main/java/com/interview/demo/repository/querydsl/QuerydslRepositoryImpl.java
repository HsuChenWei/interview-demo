package com.interview.demo.repository.querydsl;

import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuerydslRepositoryImpl implements QuerydslRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public JPAQueryFactory newQuery() {
        return new JPAQueryFactory(em);
    }

    @Override
    public <E> Querydsl dsl(Class<E> type) {
        return new Querydsl(em, new PathBuilderFactory().<E>create(type));
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


}
