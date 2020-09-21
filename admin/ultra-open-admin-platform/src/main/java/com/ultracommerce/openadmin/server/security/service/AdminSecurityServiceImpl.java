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
package com.ultracommerce.openadmin.server.security.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.email.service.EmailService;
import com.ultracommerce.common.email.service.info.EmailInfo;
import com.ultracommerce.common.event.UltraApplicationEventPublisher;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.security.util.PasswordChange;
import com.ultracommerce.common.security.util.PasswordUtils;
import com.ultracommerce.common.service.GenericResponse;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.openadmin.server.security.dao.AdminPermissionDao;
import com.ultracommerce.openadmin.server.security.dao.AdminRoleDao;
import com.ultracommerce.openadmin.server.security.dao.AdminUserDao;
import com.ultracommerce.openadmin.server.security.dao.ForgotPasswordSecurityTokenDao;
import com.ultracommerce.openadmin.server.security.domain.AdminPermission;
import com.ultracommerce.openadmin.server.security.domain.AdminRole;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.domain.ForgotPasswordSecurityToken;
import com.ultracommerce.openadmin.server.security.domain.ForgotPasswordSecurityTokenImpl;
import com.ultracommerce.openadmin.server.security.event.AdminForgotPasswordEvent;
import com.ultracommerce.openadmin.server.security.event.AdminForgotUsernameEvent;
import com.ultracommerce.openadmin.server.security.extension.AdminSecurityServiceExtensionManager;
import com.ultracommerce.openadmin.server.security.service.type.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.cache.Cache;
import javax.cache.CacheManager;


/**
 *
 * @author jfischer
 *
 */
@Service("ucAdminSecurityService")
public class AdminSecurityServiceImpl implements AdminSecurityService {

    private static final Log LOG = LogFactory.getLog(AdminSecurityServiceImpl.class);

    private static int TEMP_PASSWORD_LENGTH = 12;
    private static final int FULL_PASSWORD_LENGTH = 16;

    @Autowired
    @Qualifier("ucApplicationEventPublisher")
    protected UltraApplicationEventPublisher eventPublisher;

    @Resource(name = "ucAdminRoleDao")
    protected AdminRoleDao adminRoleDao;

    @Resource(name = "ucAdminUserDao")
    protected AdminUserDao adminUserDao;

    @Resource(name = "ucForgotPasswordSecurityTokenDao")
    protected ForgotPasswordSecurityTokenDao forgotPasswordSecurityTokenDao;

    @Resource(name = "ucAdminPermissionDao")
    protected AdminPermissionDao adminPermissionDao;
    
    @Resource(name = "ucCacheManager")
    protected CacheManager cacheManager;

    protected static String CACHE_NAME = "ucSecurityElements";
    protected static String CACHE_KEY_PREFIX = "security:";

    protected Cache<String, Boolean> cache;;

    /**
     * <p>This is simply a placeholder to be used by {@link #setupPasswordEncoder()} to determine if we're using the
     * new {@link PasswordEncoder} or the deprecated {@link org.springframework.security.authentication.encoding.PasswordEncoder PasswordEncoder}
     */
    @Resource(name="ucAdminPasswordEncoder")
    protected PasswordEncoder passwordEncoderBean;

    @Resource(name="ucEmailService")
    protected EmailService emailService;

    @Resource(name="ucSendAdminResetPasswordEmail")
    protected EmailInfo resetPasswordEmailInfo;

    @Resource(name="ucSendAdminUsernameEmailInfo")
    protected EmailInfo sendUsernameEmailInfo;

    @Resource(name = "ucAdminSecurityServiceExtensionManager")
    protected AdminSecurityServiceExtensionManager extensionManager;

    protected int getTokenExpiredMinutes() {
        return UCSystemProperty.resolveIntSystemProperty("tokenExpiredMinutes");
    }

    protected String getResetPasswordURL() {
        return UCSystemProperty.resolveSystemProperty("resetPasswordURL");
    }

    @Override
    @Transactional("ucTransactionManager")
    public void deleteAdminPermission(AdminPermission permission) {

        adminPermissionDao.deleteAdminPermission(permission);
        clearAdminSecurityCache();
    }

    @Override
    @Transactional("ucTransactionManager")
    public void deleteAdminRole(AdminRole role) {
        adminRoleDao.deleteAdminRole(role);
        clearAdminSecurityCache();
    }

    @Override
    @Transactional("ucTransactionManager")
    public void deleteAdminUser(AdminUser user) {
        adminUserDao.deleteAdminUser(user);
        clearAdminSecurityCache();
    }

    @Override
    public AdminPermission readAdminPermissionById(Long id) {
        return adminPermissionDao.readAdminPermissionById(id);
    }

    @Override
    public AdminRole readAdminRoleById(Long id) {
        return adminRoleDao.readAdminRoleById(id);
    }

    @Override
    public AdminUser readAdminUserById(Long id) {
        return adminUserDao.readAdminUserById(id);
    }

    @Override
    @Transactional("ucTransactionManager")
    public AdminPermission saveAdminPermission(AdminPermission permission) {
        permission = adminPermissionDao.saveAdminPermission(permission);
        clearAdminSecurityCache();
        return permission;
    }

    @Override
    @Transactional("ucTransactionManager")
    public AdminRole saveAdminRole(AdminRole role) {
        role = adminRoleDao.saveAdminRole(role);
        clearAdminSecurityCache();
        return role;
    }

    @Override
    @Transactional("ucTransactionManager")
    public AdminUser saveAdminUser(AdminUser user) {
        boolean encodePasswordNeeded = false;
        String unencodedPassword = user.getUnencodedPassword();

        if (user.getUnencodedPassword() != null) {
            encodePasswordNeeded = true;
            user.setPassword(unencodedPassword);
        }

        // If no password is set, default to a secure password.
        if (user.getPassword() == null) {
            user.setPassword(generateSecurePassword());
        }

        AdminUser returnUser = adminUserDao.saveAdminUser(user);

        if (encodePasswordNeeded) {
            returnUser.setPassword(encodePassword(unencodedPassword));
        }

        returnUser = adminUserDao.saveAdminUser(returnUser);
        clearAdminSecurityCache();
        return returnUser;
    }

    @Override
    public void clearAdminSecurityCache() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Admin Security Cache DELETE");
        }
        getCache().removeAll();
    }

    protected String generateSecurePassword() {
        return PasswordUtils.generateSecurePassword(FULL_PASSWORD_LENGTH);
    }

    @Override
    @Transactional("ucTransactionManager")
    public AdminUser changePassword(PasswordChange passwordChange) {
        AdminUser user = readAdminUserByUserName(passwordChange.getUsername());
        user.setUnencodedPassword(passwordChange.getNewPassword());
        user = saveAdminUser(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(passwordChange.getUsername(), passwordChange.getNewPassword(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        auth.setAuthenticated(false);
        return user;
    }

    @Override
    public boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        Boolean response = null;
        String cacheKey = buildCacheKey(adminUser, permissionType, ceilingEntityFullyQualifiedName);
        Object objectValue = getCache().get(cacheKey);

        if (objectValue != null) {
            response = (Boolean) objectValue;

            if (LOG.isTraceEnabled()) {
                LOG.trace("Admin Security Cache GET For: \"" + cacheKey + "\" = " + response);
            }
        }

        if (response == null) {
            if (extensionManager != null) {
                ExtensionResultHolder<Boolean> result = new ExtensionResultHolder<Boolean>();
                ExtensionResultStatusType resultStatusType = extensionManager.getProxy().hasPrivilegesForOperation(adminUser, permissionType, result);
                if (ExtensionResultStatusType.HANDLED == resultStatusType) {
                    response = result.getResult();
                }
            }

            if (response == null || !response) {
                response = adminPermissionDao.isUserQualifiedForOperationOnCeilingEntity(adminUser, permissionType, ceilingEntityFullyQualifiedName);

                if (!response) {
                    response = adminPermissionDao.isUserQualifiedForOperationOnCeilingEntityViaDefaultPermissions(ceilingEntityFullyQualifiedName);
                }
            }

            getCache().put(cacheKey, response);

            if (LOG.isTraceEnabled()) {
                LOG.trace("Admin Security Cache PUT For: \"" + cacheKey + "\" = " + response);
            }
        }

        return response;
    }

    protected String buildCacheKey(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        return CACHE_KEY_PREFIX
               + "user:" + adminUser.getId() + ","
               + "permType:" + permissionType.getFriendlyType() + ","
               + "ceiling:" + ceilingEntityFullyQualifiedName;
    }

    @Override
    public boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        return adminPermissionDao.doesOperationExistForCeilingEntity(permissionType, ceilingEntityFullyQualifiedName);
    }

    @Override
    public AdminUser readAdminUserByUserName(String userName) {
        return adminUserDao.readAdminUserByUserName(userName);
    }

    @Override
    public List<AdminUser> readAdminUsersByEmail(String email) {
        return adminUserDao.readAdminUserByEmail(email);
    }

    @Override
    public List<AdminUser> readAllAdminUsers() {
        return adminUserDao.readAllAdminUsers();
    }

    @Override
    public List<AdminRole> readAllAdminRoles() {
        return adminRoleDao.readAllAdminRoles();
    }

    @Override
    public List<AdminPermission> readAllAdminPermissions() {
        return adminPermissionDao.readAllAdminPermissions();
    }

    @Override
    @Transactional("ucTransactionManager")
    public GenericResponse sendForgotUsernameNotification(String emailAddress) {
        GenericResponse response = new GenericResponse();
        List<AdminUser> users = null;
        if (emailAddress != null) {
            users = adminUserDao.readAdminUserByEmail(emailAddress);
        }
        if (CollectionUtils.isEmpty(users)) {
            response.addErrorCode("notFound");
        } else {
            List<String> activeUsernames = new ArrayList<String>();
            for (AdminUser user : users) {
                if (user.getActiveStatusFlag()) {
                    activeUsernames.add(user.getLogin());
                }
            }

            if (activeUsernames.size() > 0) {
                eventPublisher.publishEvent(new AdminForgotUsernameEvent(this, emailAddress, null, activeUsernames));
            } else {
                // send inactive username found email.
                response.addErrorCode("inactiveUser");
            }
        }
        return response;
    }

    @Override
    @Transactional("ucTransactionManager")
    public GenericResponse sendResetPasswordNotification(String username) {
        GenericResponse response = new GenericResponse();
        AdminUser user = null;

        if (username != null) {
            user = adminUserDao.readAdminUserByUserName(username);
        }

        checkUser(user,response);

        if (! response.getHasErrors()) {
            String token = PasswordUtils.generateSecurePassword(TEMP_PASSWORD_LENGTH);
            token = token.toLowerCase();

            ForgotPasswordSecurityToken fpst = new ForgotPasswordSecurityTokenImpl();
            fpst.setAdminUserId(user.getId());
            fpst.setToken(encodePassword(token));
            fpst.setCreateDate(SystemTime.asDate());
            forgotPasswordSecurityTokenDao.saveToken(fpst);

            String resetPasswordUrl = getResetPasswordURL();
            if (!StringUtils.isEmpty(resetPasswordUrl)) {
                if (resetPasswordUrl.contains("?")) {
                    resetPasswordUrl=resetPasswordUrl+"&token="+token;
                } else {
                    resetPasswordUrl=resetPasswordUrl+"?token="+token;
                }
            }

            eventPublisher.publishEvent(new AdminForgotPasswordEvent(this, user.getId(), token, resetPasswordUrl));
        }
        return response;
    }

    @Override
    @Transactional("ucTransactionManager")
    public GenericResponse resetPasswordUsingToken(String username, String token, String password, String confirmPassword) {
        GenericResponse response = new GenericResponse();
        AdminUser user = null;
        if (username != null) {
            user = adminUserDao.readAdminUserByUserName(username);
        }
        checkUser(user, response);
        checkPassword(password, confirmPassword, response);
        if (StringUtils.isBlank(token)) {
            response.addErrorCode("invalidToken");
        }

        ForgotPasswordSecurityToken fpst = null;
        if (! response.getHasErrors()) {
            token = token.toLowerCase();
            List<ForgotPasswordSecurityToken> fpstoks = forgotPasswordSecurityTokenDao.readUnusedTokensByAdminUserId(user.getId());
            for (ForgotPasswordSecurityToken fpstok : fpstoks) {
                if (isPasswordValid(fpstok.getToken(), token)) {
                    fpst = fpstok;
                    break;
                }
            }
            if (fpst == null) {
                response.addErrorCode("invalidToken");
            } else if (fpst.isTokenUsedFlag()) {
                response.addErrorCode("tokenUsed");
            } else if (isTokenExpired(fpst)) {
                response.addErrorCode("tokenExpired");
            }
        }

        if (! response.getHasErrors()) {
            if (! user.getId().equals(fpst.getAdminUserId())) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Password reset attempt tried with mismatched user and token " + user.getId() + ", " + StringUtil.sanitize(token));
                }
                response.addErrorCode("invalidToken");
            }
        }

        if (! response.getHasErrors()) {
            user.setUnencodedPassword(password);
            saveAdminUser(user);
            invalidateAllTokensForAdminUser(user);
        }

        return response;
    }

    protected void invalidateAllTokensForAdminUser(AdminUser user) {
        List<ForgotPasswordSecurityToken> tokens = forgotPasswordSecurityTokenDao.readUnusedTokensByAdminUserId(user.getId());
        for (ForgotPasswordSecurityToken token : tokens) {
            token.setTokenUsedFlag(true);
            forgotPasswordSecurityTokenDao.saveToken(token);
        }
    }

    protected void checkUser(AdminUser user, GenericResponse response) {
        if (user == null) {
            response.addErrorCode("invalidUser");
        } else if (StringUtils.isBlank(user.getEmail())) {
            response.addErrorCode("emailNotFound");
        } else if (BooleanUtils.isNotTrue(user.getActiveStatusFlag())) {
            response.addErrorCode("inactiveUser");
        }
    }

    protected void checkPassword(String password, String confirmPassword, GenericResponse response) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            response.addErrorCode("invalidPassword");
        } else if (! password.equals(confirmPassword)) {
            response.addErrorCode("passwordMismatch");
        }
    }

    protected void checkExistingPassword(String unencodedPassword, AdminUser user, GenericResponse response) {
        if (!isPasswordValid(user.getPassword(), unencodedPassword)) {
            response.addErrorCode("invalidPassword");
        }
    }

    protected boolean isTokenExpired(ForgotPasswordSecurityToken fpst) {
        Date now = SystemTime.asDate();
        long currentTimeInMillis = now.getTime();
        long tokenSaveTimeInMillis = fpst.getCreateDate().getTime();
        long minutesSinceSave = (currentTimeInMillis - tokenSaveTimeInMillis)/60000;
        return minutesSinceSave > getTokenExpiredMinutes();
    }

    public static int getPASSWORD_TOKEN_LENGTH() {
        return TEMP_PASSWORD_LENGTH;
    }

    public static void setPASSWORD_TOKEN_LENGTH(int PASSWORD_TOKEN_LENGTH) {
        AdminSecurityServiceImpl.TEMP_PASSWORD_LENGTH = PASSWORD_TOKEN_LENGTH;
    }

    public EmailInfo getSendUsernameEmailInfo() {
        return sendUsernameEmailInfo;
    }

    public void setSendUsernameEmailInfo(EmailInfo sendUsernameEmailInfo) {
        this.sendUsernameEmailInfo = sendUsernameEmailInfo;
    }

    public EmailInfo getResetPasswordEmailInfo() {
        return resetPasswordEmailInfo;
    }

    public void setResetPasswordEmailInfo(EmailInfo resetPasswordEmailInfo) {
        this.resetPasswordEmailInfo = resetPasswordEmailInfo;
    }

    @Override
    @Transactional("ucTransactionManager")
    public GenericResponse changePassword(String username, String oldPassword, String password, String confirmPassword) {
        GenericResponse response = new GenericResponse();
        AdminUser user = null;
        if (username != null) {
            user = adminUserDao.readAdminUserByUserName(username);
        }
        checkUser(user, response);
        checkPassword(password, confirmPassword, response);

        if (!response.getHasErrors()) {
            checkExistingPassword(oldPassword, user, response);
        }

        if (!response.getHasErrors()) {
            user.setUnencodedPassword(password);
            saveAdminUser(user);

        }

        return response;

    }

    /**
     * Determines if a password is valid by comparing it to the encoded string, salting is handled internally to the {@link PasswordEncoder}.
     * <p>
     * This method must always be called to verify if a password is valid after the original encoded password is generated
     * due to {@link PasswordEncoder} randomly generating salts internally and appending them to the resulting hash.
     *
     * @param encodedPassword the encoded password
     * @param rawPassword the raw password to check against the encoded password
     * @return true if rawPassword matches the encodedPassword, false otherwise
     */
    protected boolean isPasswordValid(String encodedPassword, String rawPassword) {
        return passwordEncoderBean.matches(rawPassword, encodedPassword);
    }

    /**
     * Generate an encoded password from a raw password
     * <p>
     * This method can only be called once per password. The salt is randomly generated internally in the {@link PasswordEncoder}
     * and appended to the hash to provide the resulting encoded password. Once this has been called on a password,
     * going forward all checks for authenticity must be done by {@link #isPasswordValid(String, String)} as encoding the
     * same password twice will result in different encoded passwords.
     *
     * @param rawPassword the unencoded password to encode
     * @return the encoded password
     */
    protected String encodePassword(String rawPassword) {
        return passwordEncoderBean.encode(rawPassword);
    }
    
    protected Cache<String, Boolean> getCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = cacheManager.getCache(CACHE_NAME);
                }
            }
        }
        return cache;
    }
}
