/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.file.dao;

import com.ultracommerce.cms.file.domain.StaticAssetStorage;
import com.ultracommerce.cms.file.domain.StaticAssetStorageImpl;
import com.ultracommerce.common.persistence.EntityConfiguration;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by IntelliJ IDEA.
 * User: jfischer
 * Date: 9/8/11
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("ucStaticAssetStorageDao")
public class StaticAssetStorageDaoImpl implements StaticAssetStorageDao {

    @PersistenceContext(unitName = "ucCMSStorage")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public StaticAssetStorage create() {
        return (StaticAssetStorage) entityConfiguration.createEntityInstance("com.ultracommerce.cms.file.domain.StaticAssetStorage");
    }

    @Override
    public Blob createBlob(MultipartFile uploadedFile) throws IOException {
        return createBlob(uploadedFile.getInputStream(), uploadedFile.getSize());
    }
    
    @Override
    public Blob createBlob(InputStream uploadedFileInputStream, long fileSize) throws IOException {
        InputStream inputStream = uploadedFileInputStream;
        //We'll work with Blob instances and streams so that the uploaded files are never read into memory
        return em.unwrap(Session.class).getLobHelper().createBlob(inputStream, fileSize);
    }

    @Override
    public StaticAssetStorage readStaticAssetStorageById(Long id) {
        return em.find(StaticAssetStorageImpl.class, id);
    }

    @Override
    public StaticAssetStorage readStaticAssetStorageByStaticAssetId(Long id) {
        Query query = em.createNamedQuery("UC_READ_STATIC_ASSET_STORAGE_BY_STATIC_ASSET_ID");
        query.setParameter("id", id);

        return (StaticAssetStorage) query.getSingleResult();
    }

    @Override
    public StaticAssetStorage save(StaticAssetStorage assetStorage) {
        if (em.contains(assetStorage)) {
            return em.merge(assetStorage);
        }
        em.persist(assetStorage);
        em.flush();
        return assetStorage;
    }

    @Override
    public void delete(StaticAssetStorage assetStorage) {
        if (!em.contains(assetStorage)) {
            assetStorage = readStaticAssetStorageById(assetStorage.getId());
        }
        em.remove(assetStorage);
    }
}
