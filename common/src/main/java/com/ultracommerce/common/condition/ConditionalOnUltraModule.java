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
/**
 * 
 */
package com.ultracommerce.common.condition;

import com.ultracommerce.common.module.UltraModuleRegistration;
import com.ultracommerce.common.module.UltraModuleRegistration.UltraModuleEnum;
import com.ultracommerce.common.module.ModulePresentUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Allows for conditional registration of beans depending only if a particular Ultra module is present. By default, this checks for the presence of
 * an implementation entry for a {@link UltraModuleRegistration} in {@code spring.factories}.
 * 
 * <p>
 * There are 2 options for checking these registrations:
 * <ol>
 *  <li>The type-safe {@link #value()} which assumes that this class is kept up to date with different modules that are added</li>
 *  <li>The {@link #moduleName()} which maps directly to the {@link UltraModuleRegistration#getModuleName()}</li>
 * </ol>
 * 
 * <p>
 * Generally you should use the {@link #value()} attribute to give you a type-safe way to reference the registrations but it is possible
 * that you need to reference a module that has not yet been added to this class. In that case, use the {@link #moduleName()} parameter
 * which allows for more dynamic checking of module names.
 * 
 * <p>
 * If the module has not registered itself via {@code spring.factories} then this will always return false. In this case,
 * utilize one of the Spring {@code ConditionalOn...} annotations like {@link ConditionalOnClass} and consider making your own composed
 * annotation that utilizes it. For instance:
 * 
 * <pre>
 * {@literal @}Target({ ElementType.TYPE, ElementType.METHOD })
 * {@literal @}Retention(RetentionPolicy.RUNTIME)
 * {@literal @}Documented
 * {@literal @}ConditionalOnClass("com.ultracommerce.somemodule.ModuleClass")
 * public {@literal @}interface ConditionalOnSomeModule {
 * 
 * }
 * </pre>
 * 
 * <p>
 * This annotation can be used as a composed meta-annotation for module-specific annotations. Example:
 * 
 * <pre>
 * {@literal @}Target({ ElementType.TYPE, ElementType.METHOD })
 * {@literal @}Retention(RetentionPolicy.RUNTIME)
 * {@literal @}Documented
 * {@literal @}ConditionalOnUltraModule(UltraModuleEnum.ACCOUNT)
 * public {@literal @}interface ConditionalOnAccountModule {
 * 
 * }
 * </pre>
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @since 5.2
 * @see {@link ModulePresentUtil}
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnUltraModuleCondition.class)
public @interface ConditionalOnUltraModule {
    
    /**
     * Which module to check for the presence of. This should generally be preferred to using {@link #moduleName()}
     * but can be used as as stop-gap measurement if the module is not explicitly defined in {@link UltraModuleEnum}.
     */
    public UltraModuleEnum value() default UltraModuleEnum.IGNORED;
    
    /**
     * This should only be used if the module you are checking for has not yet been added to the {@link UltraModuleEnum} as that.
     * Generally you should seek to use the {@link #value()} parameter and add additional modules as needed.
     */
    public String moduleName() default "";
    
}
