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

package com.ultracommerce.openadmin.web.controller;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.i18n.domain.TranslatedEntity;
import com.ultracommerce.common.i18n.domain.Translation;
import com.ultracommerce.common.i18n.domain.TranslationImpl;
import com.ultracommerce.common.i18n.service.TranslationService;
import com.ultracommerce.common.util.UCMessageUtils;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.SectionCrumb;
import com.ultracommerce.openadmin.server.domain.PersistencePackageRequest;
import com.ultracommerce.openadmin.server.security.remote.EntityOperationType;
import com.ultracommerce.openadmin.server.security.remote.SecurityVerifier;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceThreadManager;
import com.ultracommerce.openadmin.web.controller.modal.ModalHeaderType;
import com.ultracommerce.openadmin.web.form.TranslationForm;
import com.ultracommerce.openadmin.web.form.component.ListGrid;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;
import com.ultracommerce.openadmin.web.form.entity.EntityFormAction;
import com.ultracommerce.openadmin.web.form.entity.Field;
import com.ultracommerce.openadmin.web.service.FormBuilderExtensionManager;
import com.ultracommerce.openadmin.web.service.TranslationFormAction;
import com.ultracommerce.openadmin.web.service.TranslationFormBuilderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller("ucAdminTranslationController")
@RequestMapping("/translation")
public class AdminTranslationController extends AdminAbstractController {

    @Resource(name = "ucTranslationService")
    protected TranslationService translationService;

    @Resource(name = "ucTranslationFormBuilderService")
    protected TranslationFormBuilderService formService;

    @Resource(name = "ucFormBuilderExtensionManager")
    protected FormBuilderExtensionManager formBuilderExtensionManager;
    
    @Resource(name = "ucAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;

    @Resource(name = "ucAdminTranslationControllerExtensionManager")
    protected AdminTranslationControllerExtensionManager extensionManager;

    @Resource(name = "ucPersistenceThreadManager")
    protected PersistenceThreadManager persistenceThreadManager;

    /**
     * Invoked when the translation button is clicked on a given translatable field
     * 
     * @param request
     * @param response
     * @param model
     * @param form
     * @param result
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewTranslation(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute(value = "form") TranslationForm form, BindingResult result) throws Exception {
        if (extensionManager != null) {
            extensionManager.getProxy().applyTransformation(form);
        }

        adminRemoteSecurityService.securityCheck(form.getCeilingEntity(), EntityOperationType.FETCH);

        List<Translation> translations =
                translationService.getTranslations(form.getCeilingEntity(), form.getEntityId(), form.getPropertyName());
        ListGrid lg = formService.buildListGrid(translations, form.getIsRte());

        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("form", form);
        model.addAttribute("listGrid", lg);
        model.addAttribute("viewType", "modal/translationListGrid");
        model.addAttribute("modalHeaderType", ModalHeaderType.TRANSLATION.getType());
        return MODAL_CONTAINER_VIEW;
    }

    /**
     * Renders a modal dialog that has a list grid of translations for the specified field
     * 
     * @param request
     * @param response
     * @param model
     * @param form
     * @param result
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddTranslation(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars, @ModelAttribute(value = "form") TranslationForm form,
            BindingResult result) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = new ArrayList<>();
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars);
        ClassMetadata cmd = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();

        adminRemoteSecurityService.securityCheck(form.getCeilingEntity(), EntityOperationType.FETCH);

        EntityForm entityForm = formService.buildTranslationForm(cmd, form, TranslationFormAction.ADD);
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("viewType", "modal/translationAdd");
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", ModalHeaderType.ADD_TRANSLATION.getType());
        return MODAL_CONTAINER_VIEW;
    }

    /**
     * Saves a new translation to the database. 
     * 
     * Note that if the ceiling entity, entity id, property name, and locale code match a previously existing translation,
     * this method will update that translation.
     * 
     * @param request
     * @param response
     * @param model
     * @param entityForm
     * @param result
     * @return the result of a call to {@link #viewTranslation}, which renders the list grid
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addTranslation(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute(value = "entityForm") EntityForm entityForm, BindingResult result) throws Exception {

        final TranslationForm form = getTranslationForm(entityForm);
        adminRemoteSecurityService.securityCheck(form.getCeilingEntity(), EntityOperationType.UPDATE);
        SectionCrumb sectionCrumb = new SectionCrumb();
        sectionCrumb.setSectionIdentifier(TranslationImpl.class.getName());
        List<SectionCrumb> sectionCrumbs = Arrays.asList(sectionCrumb);
        entityForm.setCeilingEntityClassname(Translation.class.getName());
        entityForm.setEntityType(TranslationImpl.class.getName());

        Field entityType = new Field();
        entityType.setName("entityType");

        String ceilingEntity = form.getCeilingEntity();

        TranslatedEntity translatedEntity = translationService.getAssignableEntityType(ceilingEntity);
        if (translatedEntity == null && ceilingEntity.endsWith("Impl")) {
            int pos = ceilingEntity.lastIndexOf("Impl");
            ceilingEntity = ceilingEntity.substring(0, pos);
            translatedEntity = TranslatedEntity.getInstance(ceilingEntity);
        }
        entityType.setValue(translatedEntity.getFriendlyType());

        Field fieldName = new Field();
        fieldName.setName("fieldName");
        fieldName.setValue(form.getPropertyName());

        entityForm.getFields().put("entityType", entityType);
        entityForm.getFields().put("fieldName", fieldName);

        String[] sectionCriteria = customCriteriaService.mergeSectionCustomCriteria(ceilingEntity, getSectionCustomCriteria());
        Entity entity = service.addEntity(entityForm, sectionCriteria, sectionCrumbs).getEntity();

        entityFormValidator.validate(entityForm, entity, result);
        if (result.hasErrors()) {
            entityForm.setPreventSubmit();
            String jsErrorMap = resultToJS(result);
            entityForm.setJsErrorMap(jsErrorMap);
            model.addAttribute("entity", entity);
            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/translationAdd");
            model.addAttribute("currentUrl", request.getRequestURL().toString());
            model.addAttribute("modalHeaderType", ModalHeaderType.ADD_TRANSLATION.getType());
            return MODAL_CONTAINER_VIEW;
        } else {
            return viewTranslation(request, response, model, form, result);
        }
    }

    /**
     * analyzes the error information, and converts it into a Javascript object  string, which can be passed to to the HTML form through the entityForm
     * @param result
     * @return
     */
    private String resultToJS(BindingResult result) {
        StringBuffer sb = new StringBuffer("[");
        List<ObjectError> errors = result.getAllErrors();
        for (ObjectError objectError : errors) {
            if (objectError instanceof FieldError) {
                FieldError ferr = (FieldError) objectError;
                sb.append("{");
                String fieldOnly = StringUtil.extractFieldNameFromExpression(ferr.getField());
                sb.append("\"").append(fieldOnly).append("\":");
                String localizedMessage = UCMessageUtils.getMessage(ferr.getDefaultMessage());
                sb.append("\"").append(localizedMessage).append("\"");
                sb.append("},");
            }
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1); //the last comma
        }
        sb.append("]");
        return sb.toString();
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String showUpdateTranslation(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars, @ModelAttribute(value = "form") TranslationForm form,
            BindingResult result) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = new ArrayList<>();
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars);
        ClassMetadata cmd = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();

        adminRemoteSecurityService.securityCheck(form.getCeilingEntity(), EntityOperationType.FETCH);

        Entity entity = service.getRecord(ppr, form.getTranslationId().toString(), cmd, false).getDynamicResultSet().getRecords()[0];

        form.setTranslatedValue(entity.findProperty("translatedValue").getValue());
        
        EntityForm entityForm = formService.buildTranslationForm(cmd, form, TranslationFormAction.UPDATE);
        entityForm.setId(entity.findProperty(service.getIdProperty(cmd)).getValue());

        formBuilderExtensionManager.getProxy().modifyPopulatedEntityForm(entityForm, entity);
        formBuilderExtensionManager.getProxy().addAdditionalFormActions(entityForm);        

        modifyRevertButton(entityForm);
        
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("viewType", "modal/translationAdd");
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", ModalHeaderType.UPDATE_TRANSLATION.getType());
        return MODAL_CONTAINER_VIEW;
    }
    
    /**
     * Updates the given translation id to the new locale code and translated value
     * 
     * @param request
     * @param response
     * @param model
     * @param entityForm
     * @param result
     * @return the result of a call to {@link #viewTranslation}, which renders the list grid
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateTranslation(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute(value = "entityForm") EntityForm entityForm, BindingResult result) throws Exception {
        final TranslationForm form = getTranslationForm(entityForm);
        adminRemoteSecurityService.securityCheck(form.getCeilingEntity(), EntityOperationType.UPDATE);
        SectionCrumb sectionCrumb = new SectionCrumb();
        sectionCrumb.setSectionIdentifier(TranslationImpl.class.getName());
        sectionCrumb.setSectionId(String.valueOf(form.getTranslationId()));
        List<SectionCrumb> sectionCrumbs = Arrays.asList(sectionCrumb);
        entityForm.setCeilingEntityClassname(Translation.class.getName());
        entityForm.setEntityType(TranslationImpl.class.getName());

        Field id = new Field();
        id.setName("id");
        id.setValue(String.valueOf(form.getTranslationId()));
        entityForm.getFields().put("id", id);
        entityForm.setId(String.valueOf(form.getTranslationId()));

        String[] sectionCriteria = customCriteriaService.mergeSectionCustomCriteria(Translation.class.getName(), getSectionCustomCriteria());
        service.updateEntity(entityForm, sectionCriteria, sectionCrumbs).getEntity();
        return viewTranslation(request, response, model, form, result);
    }

    /**
     * Deletes the translation specified by the translation id
     * 
     * @param request
     * @param response
     * @param model
     * @param id
     * @param form
     * @param result
     * @return the result of a call to {@link #viewTranslation}, which renders the list grid
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteTranslation(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars, @ModelAttribute(value = "form") final TranslationForm form,
            BindingResult result) throws Exception {
        adminRemoteSecurityService.securityCheck(form.getCeilingEntity(), EntityOperationType.UPDATE);
        SectionCrumb sectionCrumb = new SectionCrumb();
        sectionCrumb.setSectionIdentifier(TranslationImpl.class.getName());
        sectionCrumb.setSectionId(String.valueOf(form.getTranslationId()));
        List<SectionCrumb> sectionCrumbs = Arrays.asList(sectionCrumb);

        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars);
        ClassMetadata cmd = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        EntityForm entityForm = formService.buildTranslationForm(cmd, form, TranslationFormAction.OTHER);
        entityForm.setCeilingEntityClassname(Translation.class.getName());
        entityForm.setEntityType(TranslationImpl.class.getName());

        Field id = new Field();
        id.setName("id");
        id.setValue(String.valueOf(form.getTranslationId()));
        entityForm.getFields().put("id", id);
        entityForm.setId(String.valueOf(form.getTranslationId()));

        String[] sectionCriteria = customCriteriaService.mergeSectionCustomCriteria(Translation.class.getName(), getSectionCustomCriteria());
        service.removeEntity(entityForm, sectionCriteria, sectionCrumbs);
        return viewTranslation(request, response, model, form, result);
    }

    /**
     * Converts an EntityForm into a TranslationForm
     * 
     * @param entityForm
     * @return the converted translation form
     */
    protected TranslationForm getTranslationForm(EntityForm entityForm) {
        TranslationForm form = new TranslationForm();
        form.setCeilingEntity(entityForm.findField("ceilingEntity").getValue());
        form.setEntityId(entityForm.findField("entityId").getValue());
        form.setLocaleCode(entityForm.findField("localeCode").getValue());
        form.setPropertyName(entityForm.findField("propertyName").getValue());
        form.setTranslatedValue(entityForm.findField("translatedValue").getValue());
        form.setIsRte(Boolean.valueOf(entityForm.findField("isRte").getValue()));
        if (StringUtils.isNotBlank(entityForm.getId())) {
            form.setTranslationId(Long.parseLong(entityForm.getId()));
        }
        return form;
    }

    @Override
    protected String getClassNameForSection(String sectionKey) {
        return Translation.class.getName();
    }

    /**
     * Changing the default button class to "translation-revert-button" so that the JQuery action will be correct
     * @param entityForm - EntityForm where revert action will be modified
     */
    protected void modifyRevertButton(EntityForm entityForm) {
        EntityFormAction action = entityForm.findActionById("REVERT");
        if (action != null) {
            action.setButtonClass("translation-revert-button");            
        }
    }
    
}
