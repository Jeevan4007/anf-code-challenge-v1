package com.anf.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anf.core.services.ResourceResolverService;

@Component(service = ResourceResolverService.class, immediate = true)
public class ResourceResolverServiceImpl implements ResourceResolverService {

    private static final String SUBSERVICE_NAME = "anf-service-user";
    
    @Reference
    ResourceResolverFactory resolverFactory;

    @Override
    public ResourceResolver getServiceResourceResolver() throws LoginException {
        Map<String, Object> serviceMap = new HashMap<>();
        serviceMap.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME);
        return resolverFactory.getServiceResourceResolver(serviceMap);
    }
}
