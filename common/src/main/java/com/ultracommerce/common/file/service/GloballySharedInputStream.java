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

package com.ultracommerce.common.file.service;

import java.io.IOException;
import java.io.InputStream;


/**
 * This class serves as a marker interface to indicate that the resource comes from a shared area of the 
 * filesystem.    It allows multi-site implementations to only generate one copy of assets that are being
 * resized.
 * 
 * @author bpolster
 *
 */
public class GloballySharedInputStream extends InputStream {

    private InputStream parentInputStream;

    public GloballySharedInputStream(InputStream parentInputStream) {
        this.parentInputStream = parentInputStream;
    }

    public int available() throws IOException {
        return parentInputStream.available();
    }

    public void close() throws IOException {
        parentInputStream.close();
    }

    public void mark(int arg0) {
        parentInputStream.mark(arg0);
    }

    public boolean markSupported() {
        return parentInputStream.markSupported();
    }

    public int read() throws IOException {
        return parentInputStream.read();
    }

    public int read(byte[] arg0, int arg1, int arg2) throws IOException {
        return parentInputStream.read(arg0, arg1, arg2);
    }

    public int read(byte[] arg0) throws IOException {
        return parentInputStream.read(arg0);
    }

    public void reset() throws IOException {
        parentInputStream.reset();
    }

    public long skip(long arg0) throws IOException {
        return parentInputStream.skip(arg0);
    }

}
