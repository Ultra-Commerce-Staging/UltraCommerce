/*
 * #%L
 * UltraCommerce Integration
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.testng.annotations.Test;

import com.tngtech.archunit.base.Optional;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.io.Serializable;

/**
 * @author Chad Harchar (charchar)
 */
public class EqualsHashCodeMethodsTest {

    private static final String BASE_PACKAGE = "com.ultracommerce";

    private JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(BASE_PACKAGE);

    @Test
    public void equalsAndHashCodeMustExistTogether() {

        ArchRule rule =
                classes().that().implement(Serializable.class)
                        .should(equalsAndHashCodeMatch());

        rule.check(importedClasses);
    }

    private static ArchCondition<JavaClass> equalsAndHashCodeMatch() {
        return new ArchCondition<JavaClass>("implement equals and hashCode methods") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                String msg = "class " + item.getName();
                Optional<JavaMethod> equalsMethod = item.tryGetMethod("equals", Object.class);
                Optional<JavaMethod> hashcodeMethod = item.tryGetMethod("hashCode");
                
                if (equalsMethod.isPresent() == hashcodeMethod.isPresent()) {
                    events.add(SimpleConditionEvent.satisfied(item, msg + " match satisfy contract"));
                } else {
                    events.add(SimpleConditionEvent.violated(item, msg + " if one of 'equals()' or 'hashCode()' exists, the other must too"));
                }   
            }
        };
    }
}
