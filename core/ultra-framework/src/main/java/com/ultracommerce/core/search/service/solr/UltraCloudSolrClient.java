/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.search.service.solr;

import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Nick Crum (ncrum)
 *
 * @deprecated Functionality for determining the default collection has been moved to {@link SolrConfiguration} 
 * therefore there's no need to use the UltraCloudSolrClient. Simply use {@link CloudSolrClient}
 */
@Deprecated
public class UltraCloudSolrClient extends CloudSolrClient {

    protected SolrConfiguration solrConfig;
    protected boolean reindexClient;

    public UltraCloudSolrClient(String zkHost) {
        super(createBuilder(Arrays.asList(zkHost), null, null));
    }

    public UltraCloudSolrClient(String zkHost, HttpClient httpClient) {
        super(createBuilder(Arrays.asList(zkHost), null, httpClient));
    }

    public UltraCloudSolrClient(Collection<String> zkHosts, String chroot) {
        super(createBuilder(zkHosts, chroot, null));
    }

    public UltraCloudSolrClient(Collection<String> zkHosts, String chroot, HttpClient httpClient) {
        super(createBuilder(zkHosts, chroot, httpClient));
    }

    public UltraCloudSolrClient(String zkHost, boolean updatesToLeaders) {
        super(createBuilder(zkHost, Optional.of(updatesToLeaders), null));
    }

    public UltraCloudSolrClient(String zkHost, boolean updatesToLeaders, HttpClient httpClient) {
        super(createBuilder(zkHost, Optional.of(updatesToLeaders), httpClient));
    }

    public UltraCloudSolrClient(String zkHost, LBHttpSolrClient lbClient) {
        super(createBuilder(zkHost, lbClient, null));
    }

    public UltraCloudSolrClient(String zkHost, LBHttpSolrClient lbClient, boolean updatesToLeaders) {
        super(createBuilder(zkHost, lbClient, Optional.of(updatesToLeaders)));
    }

    private static CloudSolrClient.Builder createBuilder(Collection<String> zkHosts, String chroot, HttpClient httpClient) {
        return new Builder(new ArrayList<>(zkHosts), Optional.of(chroot)).withHttpClient(httpClient);
    }

    private static CloudSolrClient.Builder createBuilder(String zkHost, Optional<Boolean> updatesToLeaders, HttpClient httpClient) {
        Builder builder = new Builder(Arrays.asList(zkHost), null).withHttpClient(httpClient);
        if (updatesToLeaders.orElse(true)) {
            builder.sendUpdatesOnlyToShardLeaders();
        } else {
            builder.sendUpdatesToAllReplicasInShard();
        }
        return builder;
    }

    private static CloudSolrClient.Builder createBuilder(String zkHost, LBHttpSolrClient lbClient, Optional<Boolean> updatesToLeaders) {
        Builder builder = new Builder(Arrays.asList(zkHost), null).withLBHttpSolrClient(lbClient);
        if (updatesToLeaders.orElse(true)) {
            builder.sendUpdatesOnlyToShardLeaders();
        } else {
            builder.sendUpdatesToAllReplicasInShard();
        }
        return builder;
    }

    public boolean isReindexClient() {
        return reindexClient;
    }

    public void setReindexClient(boolean reindexClient) {
        this.reindexClient = reindexClient;
    }
    
    public SolrConfiguration getSolrConfig() {
        return solrConfig;
    }
    
    public void setSolrConfig(SolrConfiguration solrConfig) {
        this.solrConfig = solrConfig;
    }
}
