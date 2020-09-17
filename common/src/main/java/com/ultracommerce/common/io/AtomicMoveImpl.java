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
package com.ultracommerce.common.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.util.Map;
import java.util.WeakHashMap;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @see AtomicMove
 * @author Jeff Fischer
 */
public class AtomicMoveImpl implements AtomicMove {

    private static final Map<String, Object> FILE_MOVE_LOCKS = new WeakHashMap<>();

    @Override
    public void replaceExisting(File src, File dest) throws IOException {
        synchronized (getFileMoveLock(dest)) {
            if (src.exists()) {
                try {
                    Files.move(src.toPath(), dest.toPath(), ATOMIC_MOVE, REPLACE_EXISTING);
                } catch (AtomicMoveNotSupportedException e) {
                    Files.move(src.toPath(), dest.toPath(), REPLACE_EXISTING);
                }
            }
        }
    }

    protected Object getFileMoveLock(File file) {
        String filePath = file.getAbsolutePath();
        synchronized (FILE_MOVE_LOCKS) {
            if (!FILE_MOVE_LOCKS.containsKey(filePath)) {
                FILE_MOVE_LOCKS.put(filePath, new Object());
            }
            return FILE_MOVE_LOCKS.get(filePath);
        }
    }
}
