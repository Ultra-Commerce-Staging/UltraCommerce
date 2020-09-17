/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.sandbox.domain;

import com.ultracommerce.common.persistence.Status;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface SandBox extends Serializable, Status {

    Long getId();

    void setId(Long id);

    /**
     * The name of the sandbox.
     * Certain sandbox names are reserved in the system.    User created
     * sandboxes cannot start with "", "approve_", or "deploy_".
     *
     * @return String sandbox name
     */
    String getName();

    void setName(String name);

    SandBoxType getSandBoxType();

    void setSandBoxType(SandBoxType sandBoxType);

    Long getAuthor();

    void setAuthor(Long author);

    SandBox getParentSandBox();

    void setParentSandBox(SandBox parentSandBox);

    String getColor();

    void setColor(String color);

    Date getGoLiveDate();

    void setGoLiveDate(Date goLiveDate);

    List<Long> getSandBoxIdsForUpwardHierarchy(boolean includeInherited);

    List<Long> getSandBoxIdsForUpwardHierarchy(boolean includeInherited, boolean includeCurrent);

    List<SandBox> getChildSandBoxes();

    void setChildSandBoxes(List<SandBox> childSandBoxes);

    /**
     * @return whether or not this sandbox, or any of its parent sandboxes, has type DEFAULT.
     */
    public boolean getIsInDefaultHierarchy();

    public void setArchived(Character archived);

    public Character getArchived();

    public boolean isActive();

}


