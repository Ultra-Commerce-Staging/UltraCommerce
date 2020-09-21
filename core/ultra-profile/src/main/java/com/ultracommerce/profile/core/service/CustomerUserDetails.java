/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created in order to utilize the Customer's primary key to salt passwords with. This allows username changes without
 * requiring a password reset since the primary key should never change.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link UserDetailsServiceImpl}
 * @see {@link CustomerServiceImpl#getSalt(com.ultracommerce.profile.core.domain.Customer)}
 */
public class CustomerUserDetails extends User {

    private static final long serialVersionUID = 1L;
    
    protected Long id;
    
    public CustomerUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(id, username, password, true, true, true, true, authorities);
    }
    
    public CustomerUserDetails(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
    
    public CustomerUserDetails withId(Long id) {
        setId(id);
        return this;
    }
    
    /**
     * @return the primary key of the Customer
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the primary key of the Customer
     */
    public void setId(Long id) {
        this.id = id;
    }
    
}
