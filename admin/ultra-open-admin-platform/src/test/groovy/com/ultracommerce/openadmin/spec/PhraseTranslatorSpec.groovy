/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.spec

import com.ultracommerce.openadmin.web.rulebuilder.UCOperator
import com.ultracommerce.openadmin.web.rulebuilder.statement.Expression
import com.ultracommerce.openadmin.web.rulebuilder.statement.PhraseTranslator
import spock.lang.Specification


/**
 * @author Elbert Bautista (elbertbautista)
 */
class PhraseTranslatorSpec extends Specification {

    def "Boolean test with Map Field Separator"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator()
        String phrase = "MvelHelper.convertField(\"BOOLEAN\",orderItem.?product.?getProductAttributes()[\"myboolean\"])==true"

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("product.getProductAttributes()---myboolean");
        expression.operator.equals(UCOperator.EQUALS);
        expression.value.equals("true");
    }

    def "Decimal test with Map Field Separator"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator()
        String phrase = "MvelHelper.convertField(\"DECIMAL\",orderItem.?product.?getProductAttributes()[\"mymoney\"])>0"

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("product.getProductAttributes()---mymoney");
        expression.operator.equals(UCOperator.GREATER_THAN);
        expression.value.equals("0");
    }

    def "Legacy CollectionUtils intersection test with Map Field Separator"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator()
        String phrase = "CollectionUtils.intersection(orderItem.?product.?getProductAttributes()[\"myenum\"],[\"test1\",\"test2\"]).size()>0"

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("product.getProductAttributes()---myenum");
        expression.operator.equals(UCOperator.COLLECTION_IN);
        expression.value.equals("[\"test1\",\"test2\"]");
    }

    def "Bean Null-Safe CollectionUtils intersection test with Map Field Separator"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator()
        String phrase = "CollectionUtils.intersection(orderItem.?product.?getProductAttributes().?get(\"myenum\").?getValue(),[\"test1\",\"test2\"]).size()>0"

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("product.getProductAttributes()---myenum");
        expression.operator.equals(UCOperator.COLLECTION_IN);
        expression.value.equals("[\"test1\",\"test2\"]");
    }

    def "MVELHelper Null-Safe CollectionUtils intersection test with Map Field Separator"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator()
        String phrase = "CollectionUtils.intersection(orderItem.?product.?getProductAttributes().?get(\"myenum\").?value,[\"test1\",\"test2\"]).size()>0"

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("product.getProductAttributes()---myenum");
        expression.operator.equals(UCOperator.COLLECTION_IN);
        expression.value.equals("[\"test1\",\"test2\"]");
    }

    def "Legacy String Contains Test"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator();
        String phrase = "MvelHelper.toUpperCase(pricingContext.?localeCode).contains(MvelHelper.toUpperCase(\"MX\"))";

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("localeCode");
        expression.operator.equals(UCOperator.ICONTAINS);
        expression.value.equals("MX");
    }

    def "String Contains Test"() {
        setup: "Initialize the PhraseTranslator and test data"
        PhraseTranslator translator = new PhraseTranslator();
        String phrase = "org.apache.commons.lang3.StringUtils.contains(MvelHelper.toUpperCase(pricingContext.?localeCode),MvelHelper.toUpperCase(\"MX\"))";

        when: "PhraseTranslator is executed"
        Expression expression = translator.createExpression(phrase);

        then: "The correct expression is produced"
        expression.field.equals("localeCode");
        expression.operator.equals(UCOperator.ICONTAINS);
        expression.value.equals("MX");
    }

}
