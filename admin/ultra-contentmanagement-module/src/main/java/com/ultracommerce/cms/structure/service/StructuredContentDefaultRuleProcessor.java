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
package com.ultracommerce.cms.structure.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.rule.AbstractRuleProcessor;
import com.ultracommerce.common.structure.dto.StructuredContentDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * By default, this rule processor combines all of the rules from
 * {@link com.ultracommerce.cms.structure.domain.StructuredContent#getStructuredContentMatchRules()}
 * into a single MVEL expression.
 *
 * @author bpolster.
 *
 */
@Service("ucContentDefaultRuleProcessor")
public class StructuredContentDefaultRuleProcessor extends AbstractRuleProcessor<StructuredContentDTO> {
    private static final Log LOG = LogFactory.getLog(StructuredContentDefaultRuleProcessor.class);

    /**
     * Returns true if all of the rules associated with the passed in <code>StructuredContent</code>
     * item match based on the passed in vars.
     *
     * Also returns true if no rules are present for the passed in item.
     *
     * @param sc - a structured content item to test
     * @param vars - a map of objects used by the rule MVEL expressions
     * @return the result of the rule checks
     */
    @Override
    public boolean checkForMatch(StructuredContentDTO sc, Map<String, Object> vars) {
        String ruleExpression = sc.getRuleExpression();

        if (ruleExpression != null) {
            if (LOG.isTraceEnabled())  {
                LOG.trace("Processing content rule for StructuredContent with id " + sc.getId() +".   Value = " + ruleExpression);
            }
            boolean result = executeExpression(ruleExpression, vars);
            if (! result) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Content failed to pass rule and will not be included for StructuredContent with id " + sc.getId() +".   Value = " + ruleExpression);
                }
            }

            return result;
        } else {
            // If no rule found, then consider this a match.
            return true;
        }
    }
    
    @Override
    @SuppressWarnings("serial")
    public Map<String, String> getContextClassNames() {
        return new HashMap<String, String>(){{
            put("customer", "com.ultracommerce.profile.core.domain.Customer");
            put("product", "com.ultracommerce.core.catalog.domain.Product");
            put("time", "com.ultracommerce.common.TimeDTO");
            put("request", "com.ultracommerce.common.RequestDTO");
        }};
    }
    
}
