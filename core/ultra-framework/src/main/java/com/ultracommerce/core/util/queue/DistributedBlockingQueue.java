/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.util.queue;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

/**
 * Interface that defines a distributed {@link BlockingQueue}, which means that this queue can be created, distributed, and operated on by multiple 
 * @author Kelly Tisdell
 *
 * @param <E>
 */
public interface DistributedBlockingQueue<E extends Serializable> extends BlockingQueue<E> {

    /**
     * {@link RuntimeException} indicating that there was an error operating on the queue, or changing queue state.
     * 
     * @author Kelly Tisdell
     *
     */
    public class DistributedQueueException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DistributedQueueException() {
            super();
        }

        public DistributedQueueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public DistributedQueueException(String message, Throwable cause) {
            super(message, cause);
        }

        public DistributedQueueException(String message) {
            super(message);
        }

        public DistributedQueueException(Throwable cause) {
            super(cause);
        }
        
        
    }
}
