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
package com.ultracommerce.common.event;

import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base abstract ApplicationEvent that provides a marker for Ultra events and provides a default 
 * context map. 
 * 
 * @see <code>com.ultracommerce.common.event.UltraApplicationEventMultiCaster</code>
 * @see <code>com.ultracommerce.common.event.UltraApplicationListener</code>
 * 
 * @author Kelly Tisdell
 *
 */
public abstract class UltraApplicationEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public static final class ContextVars {
		public static final String SITE_ID = "_SITE_ID";
		public static final String CATALOG_ID = "_CATALOG_ID";
		public static final String PROFILE_ID = "_PROFILE_ID";
		public static final String LOCALE_CODE = "_LOCALE_CODE";
		public static final String CURRENCY_CODE = "_CURRENCY_CODE";
		public static final String TIMEZONE_ID = "_TIMEZONE_ID";
	}
	
	protected transient final Map<String, Object> context = Collections.synchronizedMap(new HashMap<String, Object>());

	public UltraApplicationEvent(Object source) {
		super(source);

		UltraRequestContext ctx = UltraRequestContext.getUltraRequestContext();
		if (ctx != null) {
			if (ctx.getNonPersistentSite() != null) {
				context.put(UltraApplicationEvent.ContextVars.SITE_ID, ctx.getNonPersistentSite().getId());
			}

			if (ctx.getCurrentCatalog() != null) {
				context.put(UltraApplicationEvent.ContextVars.CATALOG_ID, ctx.getCurrentCatalog().getId());
			}

			if (ctx.getCurrentProfile() != null) {
				context.put(UltraApplicationEvent.ContextVars.PROFILE_ID, ctx.getCurrentProfile().getId());
			}

			if (ctx.getLocale() != null) {
				context.put(UltraApplicationEvent.ContextVars.LOCALE_CODE, ctx.getLocale().getLocaleCode());
			}

			if (ctx.getUltraCurrency() != null) {
				context.put(UltraApplicationEvent.ContextVars.CURRENCY_CODE, ctx.getUltraCurrency().getCurrencyCode());
			}

			if (ctx.getTimeZone() != null) {
				context.put(UltraApplicationEvent.ContextVars.TIMEZONE_ID, ctx.getTimeZone().getID());
			}
		}
	}
	
	/**
	 * Context map that allows generic objects / properties to be passed around on events. This map is synchronized.
	 * @return
	 */
	public Map<String, Object> getContext() {
		return context;
	}

	public Long getSiteId() {
		return (Long) context.get(ContextVars.SITE_ID);
	}

	public Long getCatalogId() {
		return (Long) context.get(ContextVars.CATALOG_ID);
	}

	public Long getProfileId() {
		return (Long) context.get(ContextVars.PROFILE_ID);
	}

	public String getLocaleCode() {
		return (String) context.get(ContextVars.LOCALE_CODE);
	}

	public String getCurrencyCode() {
		return (String) context.get(ContextVars.CURRENCY_CODE);
	}

	public String getTimeZoneId() {
		return (String) context.get(ContextVars.TIMEZONE_ID);
	}

}
