package com.anf.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.Designate;

import com.anf.core.services.ApplicationConfigService;
import com.anf.core.services.ApplicationServiceConfiguration;

/**
 * Service class used to fetch all global configurations - Root page
 *
 */
@Component(service = ApplicationConfigService.class)
@Designate(ocd = ApplicationServiceConfiguration.class)
@ServiceDescription("Application Config Service")
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

    /**
     * ApplicationServiceConfiguration object
     */

    private ApplicationServiceConfiguration config;

    /**
     * Activate SalesforceDealerConfigServiceImpl.
     *
     * @param config
     *            application default configs value
     */

    @Activate
    public void activate(ApplicationServiceConfiguration config) {
        this.config = config;
    }

    /**
     * Attribute for Query root path.
     *
     * @return siteRootPath
     */
    @Override
    public final String getQueryPageRoot() {
        return config.queryPageRoot();
    }

}
