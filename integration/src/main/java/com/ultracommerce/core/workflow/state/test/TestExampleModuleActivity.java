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
package com.ultracommerce.core.workflow.state.test;

import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ModuleActivity;
import com.ultracommerce.core.workflow.ProcessContext;


/**
 * Pass-through activity to test that a workflow with a {@link ModuleActivity} marker interface in it performs correctly
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public class TestExampleModuleActivity extends BaseActivity<ProcessContext<? extends Object>> implements ModuleActivity {

    @Override
    public ProcessContext<? extends Object> execute(ProcessContext<? extends Object> context) throws Exception {
        return context;
    }

    @Override
    public String getModuleName() {
        return "integration";
    }

}
