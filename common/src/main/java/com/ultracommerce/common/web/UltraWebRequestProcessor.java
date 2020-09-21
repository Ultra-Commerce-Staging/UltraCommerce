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
package com.ultracommerce.common.web;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Generic interface that should be used for processing requests from Servlet Filters, Spring interceptors or Portlet
 * filters. Note that the actual type of the request passed in should be something that extends {@link NativeWebRequest}.
 * 
 * Example usage by a Servlet Filter:
 * 
 * <pre>
 *   public class SomeServletFilter extends GenericFilterBean {
 *      &#064;Resource(name="ucCustomerStateRequestProcessor")
 *      protected UltraWebRequestProcessor processor;
 *      
 *      public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
 *          processor.process(new ServletWebRequest(request, response));
 *      }
 *   }
 * </pre>
 * 
 * <p>Also note that you should always instantiate the {@link WebRequest} with as much information available. In the above
 * example, this means using both the {@link HttpServletRequest} and {@link HttpServletResponse} when instantiating the
 * {@link ServletWebRequest}</p>
 * 
 * @author Phillip Verheyden
 * @see {@link NativeWebRequest}
 * @see {@link ServletWebRequest}
 * @see {@link org.springframework.web.portlet.context.PortletWebRequest}
 * @see {@link UltraRequestFilter}
 */
public interface UltraWebRequestProcessor {

    /**
     * Process the current request. Examples would be setting the currently logged in customer on the request or handling
     * anonymous customers in session
     * 
     * @param request
     */
    public void process(WebRequest request);
    
    /**
     * Should be called if work needs to be done after the request has been processed.
     * 
     * @param request
     */
    public void postProcess(WebRequest request);

}
