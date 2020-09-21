/*
 * #%L
 * UltraCommerce Profile Web
 * %%
 * Copyright (C) 2009 - 2016 Ultra Commerce
 * %%
 * Licensed under the Ultra Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.ultracommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Ultra in which case
 * the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */

package com.ultracommerce.common.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

/**
 * Provides methods to easily access entities managed by the 'ucPU' {@link PersistenceUnit}.
 */
public interface GenericEntityDao {

    /**
     * Finds a generic entity by a classname and id
     *
     * @param clazz
     * @param id
     * @return the entity
     */
    public <T> T readGenericEntity(Class<T> clazz, Object id);

    /**
     * For a given className, finds the parent implementation class as defined in the {@link EntityConfiguration}
     * 
     * @param className
     * @return the impl class object
     */
    public Class<?> getImplClass(String className);

    /**
     * Finds the ceiling implementation for the entity
     *
     * @param className
     * @return
     */
    Class<?> getCeilingImplClass(String className);

    /**
     * Saves a generic entity
     * 
     * @param object
     * @return the persisted version of the entity
     */
    public <T> T save(T object);

    /**
     * Persist the new entity
     *
     * @param object
     */
    void persist(Object object);

    /**
     * Remove the entity
     *
     * @param object
     */
    void remove(Object object);

    /**
     * Finds how many of the given entity class are persisted
     * 
     * @param clazz
     * @return the count of the generic entity
     */
    public <T> Long readCountGenericEntity(Class<T> clazz);

    /**
     * Finds all generic entities for a given classname, with pagination options.
     * 
     * @param clazz
     * @param limit
     * @param offset
     * @return the entities
     */
    public <T> List<T> readAllGenericEntity(Class<T> clazz, int limit, int offset);

    <T> List<T> readAllGenericEntity(Class<T> clazz);

    List<Long> readAllGenericEntityId(Class<?> clazz);

    /**
     * Retrieve the identifier from the Hibernate entity (the entity must reside in the current session)
     *
     * @param entity
     * @return
     */
    Serializable getIdentifier(Object entity);

    /**
     * Flush changes to the persistence store
     */
    void flush();

    void clearAutoFlushMode();

    void enableAutoFlushMode();

    /**
     * Clear level 1 cache
     */
    void clear();

    /**
     * Whether or not the current hibernate session (level 1) contains the object
     *
     * @param object
     * @return
     */
    boolean sessionContains(Object object);

    /**
     * Whether or not this object is an {@link javax.persistence.Entity} and whether or not it already has an id assigned
     * @param object
     * @return
     */
    boolean idAssigned(Object object);

    /**
     * Gathers the {@link EntityManager} that is based on ucPU
     *
     * @return the {@link EntityManager}
     */
    EntityManager getEntityManager();
}
