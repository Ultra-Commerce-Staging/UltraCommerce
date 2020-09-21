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
package com.ultracommerce.common.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.EfficientLRUMap;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractRuleProcessor<T> implements RuleProcessor<T> {
    
    protected final Log LOG = LogFactory.getLog(this.getClass());

    protected ParserContext parserContext;
    protected Map<String, String> contextClassNames = new HashMap<String, String> ();

    /**
     * Having a parser context that imports the classes speeds MVEL by up to 60%.
     */
    protected ParserContext getParserContext() {
        if (parserContext == null) {
            parserContext = new ParserContext();
            parserContext.addImport("MVEL", MVEL.class);
            parserContext.addImport("MvelHelper", MvelHelper.class);
        }
        return parserContext;
    }

    /**
     * Helpful method for processing a boolean MVEL expression and associated arguments.
     *
     * Caches the expression in an LRUCache.
     * @param expression
     * @param vars
     * @return the result of the expression
     */
    protected Boolean executeExpression(String expression, Map<String, Object> vars) {
        return MvelHelper.evaluateRule(expression, vars);
    }

    /**
     * List of class names to add to the MVEL ParserContext.
     * @see {@link ParserContext}
     */
    public Map<String, String> getContextClassNames() {
        return contextClassNames;
    }

    /**
     * List of class names to add to the MVEL ParserContext.
     * @see {@link ParserContext}
     */
    public void setContextClassNames(Map<String, String> contextClassNames) {
        this.contextClassNames = contextClassNames;
    }

}
