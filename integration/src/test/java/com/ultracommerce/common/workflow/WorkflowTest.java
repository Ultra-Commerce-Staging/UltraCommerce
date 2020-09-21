/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.common.workflow;

import com.ultracommerce.core.pricing.service.workflow.TotalActivity;
import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ModuleActivity;
import com.ultracommerce.core.workflow.PassThroughActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.SequenceProcessor;
import com.ultracommerce.core.workflow.state.test.TestExampleModuleActivity;
import com.ultracommerce.core.workflow.state.test.TestRollbackActivity;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import javax.annotation.Resource;


/**
 * 
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@ContextHierarchy(@ContextConfiguration(name = "siteRoot"))
public class WorkflowTest extends TestNGSiteIntegrationSetup {
    
    
    @ImportResource("classpath:uc-applicationContext-test-module.xml")
    @Configuration
    public static class WorkflowTestConfig {}
    
    @Resource(name = "ucCheckoutWorkflowActivities")
    protected List<Activity<ProcessContext<? extends Object>>> activities;
    
    @Resource(name = "ucCheckoutWorkflow")
    protected SequenceProcessor checkoutWorkflow;
    
    @Resource(name = "ucTotalActivity")
    protected TotalActivity totalActivity;
    
    
    @Test
    public void testMergedOrderedActivities() {
        Assert.assertEquals(activities.get(0).getClass(), PassThroughActivity.class);
        Assert.assertEquals(activities.get(0).getOrder(), 100);
        
        Assert.assertEquals(activities.get(6).getClass(), PassThroughActivity.class);
        Assert.assertEquals(activities.get(5).getOrder(), 3000);
    }
    
    @Test
    public void testFrameworkOrderingChanged() {
        Assert.assertEquals(totalActivity.getOrder(), 8080);
    }
    
    @Test
    public void testDetectedModuleActivity() {
        List<ModuleActivity> moduleActivities = checkoutWorkflow.getModuleActivities();
        Assert.assertEquals(moduleActivities.size(), 1);
        Assert.assertEquals(moduleActivities.get(0).getModuleName(), "integration");
    }
    
    @Test
    public void testNonExplicitOrdering() {
        Assert.assertEquals(activities.get(activities.size() - 1).getClass(), TestExampleModuleActivity.class);
        Assert.assertEquals(activities.get(activities.size() - 1).getOrder(), Ordered.LOWEST_PRECEDENCE);
    }
    
    /**
     * Tests that a merged activity can have the same order as a framework activity and come after it
     */
    @Test
    public void testSameOrderingConfiguredActivity() {
        Assert.assertEquals(activities.get(9).getClass(), TestRollbackActivity.class);
    }
    
    @Test
    public void testInBetweenActivity() {
        Assert.assertEquals(activities.get(6).getClass(), PassThroughActivity.class);
    }
    
}
