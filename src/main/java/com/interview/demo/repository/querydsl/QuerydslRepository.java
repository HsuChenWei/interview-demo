package com.interview.demo.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.Querydsl;

import javax.persistence.EntityManager;

public interface QuerydslRepository {
    JPAQueryFactory newQuery();
    <E> Querydsl dsl(Class<E> type);
    EntityManager getEntityManager();
}
