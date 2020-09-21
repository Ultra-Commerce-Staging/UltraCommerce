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
package com.ultracommerce.common.util.sql.importsql;

import com.ultracommerce.common.logging.SupportLogManager;
import com.ultracommerce.common.logging.SupportLogger;
import org.hibernate.tool.hbm2ddl.SingleLineSqlCommandExtractor;

import java.io.Reader;

/**
 * This is a utility class that is only meant to be used for testing the UC demo on HSQLDB. This replaces any of the demo
 * insert SQL statements with HSQLDB-compatible syntax.
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public class DemoHsqlSingleLineSqlCommandExtractor extends SingleLineSqlCommandExtractor {

    private static final SupportLogger LOGGER = SupportLogManager.getLogger("UserOverride", DemoHsqlSingleLineSqlCommandExtractor.class);

    @Override
    public String[] extractCommands(Reader reader) {
        String[] commands = super.extractCommands(reader);
        String[] newCommands = new String[commands.length];
        int i = 0;
        for (String command : commands) {
            String newCommand = command;
            
            // Any MySQL-specific newlines replace with special character newlines
            newCommand = newCommand.replaceAll(DemoPostgresSingleLineSqlCommandExtractor.NEWLINE_REPLACEMENT_REGEX, "' || CHAR(13) || CHAR(10) || '");

            //remove the double backslashes - hsql does not honor backslash as an escape character
            newCommand = newCommand.replaceAll(DemoSqlServerSingleLineSqlCommandExtractor.DOUBLEBACKSLASHMATCH, "\\\\");

            //replace escaped double quotes (\") with encoded double quote
            newCommand = newCommand.replaceAll("\\\\\"", "' || CHAR(34) || '");

            newCommands[i] = newCommand;
            i++;
        }
        return newCommands;
    }

}
