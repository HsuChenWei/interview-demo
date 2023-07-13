package com.interview.demo.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class SnowflakeIDGenerator extends SequenceStyleGenerator {

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(type, properties, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return String.valueOf(Snowflake.INSTANCE.nextId());
    }
}
