/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.expression;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Nick Crum ncrum
 */
@Component("ucExpressionParser")
public class UltraExpressionParserImpl implements UltraExpressionParser {

    protected final ExpressionParser parser;
    protected final List<PropertyAccessor> propertyAccessors;

    public UltraExpressionParserImpl() {
        this.parser =  new SpelExpressionParser();
        this.propertyAccessors = Arrays.asList(new MapAccessor(), new ReflectivePropertyAccessor());
    }

    @Override
    public String parseExpression(String expressionString, Map<String, Object> context) {
        return parseExpression(expressionString, context, String.class);
    }

    @Override
    public <T> T parseExpression(String expressionString, Map<String, Object> context, Class<T> targetType) {
        StandardEvaluationContext spelContext = createStandardEvaluationContext(context);
        spelContext.setPropertyAccessors(getPropertyAccessors());
        Expression expression = getExpressionParser().parseExpression(expressionString, getParserContext());
        return expression.getValue(spelContext, targetType);
    }

    protected ParserContext getParserContext() {
        return new TemplateParserContext();
    }

    protected List<PropertyAccessor> getPropertyAccessors() {
        return propertyAccessors;
    }

    protected StandardEvaluationContext createStandardEvaluationContext(Map<String, Object> context) {
        return new StandardEvaluationContext(context);
    }

    /**
     * @return the current expression parser
     */
    protected ExpressionParser getExpressionParser() {
        return parser;
    }
}
