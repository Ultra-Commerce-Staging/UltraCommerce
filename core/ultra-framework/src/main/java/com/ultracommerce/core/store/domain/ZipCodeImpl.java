/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.store.domain;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_ZIP_CODE")
public class ZipCodeImpl implements Serializable, ZipCode {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ZIP_CODE_ID", nullable = false)
    private String id;

    @Column(name = "ZIPCODE", insertable = false, updatable = false)
    @Index(name="ZIPCODE_ZIP_INDEX", columnNames={"ZIPCODE"})
    private Integer zipcode;

    @Column(name = "ZIP_STATE", insertable = false, updatable = false)
    @Index(name="ZIPCODE_STATE_INDEX", columnNames={"ZIP_STATE"})
    private String zipState;

    @Column(name = "ZIP_CITY")
    @Index(name="ZIPCODE_CITY_INDEX", columnNames={"ZIP_CITY"})
    private String zipCity;

    @Column(name = "ZIP_LONGITUDE")
    @Index(name="ZIPCODE_LONGITUDE_INDEX", columnNames={"ZIP_LONGITUDE"})
    private double zipLongitude;

    @Column(name = "ZIP_LATITUDE")
    @Index(name="ZIPCODE_LATITUDE_INDEX", columnNames={"ZIP_LATITUDE"})
    private double zipLatitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public String getZipState() {
        return zipState;
    }

    public void setZipState(String zipState) {
        this.zipState = zipState;
    }

    public String getZipCity() {
        return zipCity;
    }

    public void setZipCity(String zipCity) {
        this.zipCity = zipCity;
    }

    public double getZipLongitude() {
        return zipLongitude;
    }

    public void setZipLongitude(double zipLongitude) {
        this.zipLongitude = zipLongitude;
    }

    public double getZipLatitude() {
        return zipLatitude;
    }

    public void setZipLatitude(double zipLatitude) {
        this.zipLatitude = zipLatitude;
    }

}
