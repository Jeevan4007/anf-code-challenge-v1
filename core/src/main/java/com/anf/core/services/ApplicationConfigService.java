package com.anf.core.services;

/**
 * Interface for Application Config Service - ApplicationConfigServiceImpl.
 */
public interface ApplicationConfigService {

    String getQueryPageRoot();
    
    String getCountryDAMJson();
    
    String getAgeLimitPath();
    
    String getAnfFormPath();
}
