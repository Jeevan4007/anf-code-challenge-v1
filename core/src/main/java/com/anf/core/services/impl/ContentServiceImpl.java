package com.anf.core.services.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.constants.Constants;
import com.anf.core.services.ApplicationConfigService;
import com.anf.core.services.ContentService;
import com.anf.core.services.ResourceResolverService;
import com.day.cq.commons.jcr.JcrConstants;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {
    
    private Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
    
    @Reference
    ApplicationConfigService application;
    
    @Reference
    ResourceResolverService resourceResolverService;

    @Override
    public void commitUserDetails(String firstName, String lastName, String country, String age, ResourceResolver resourceResolver) {
        
        try {

            Map<String, Object> userInputValues = new HashMap<>();
            userInputValues.put("jcr:primaryType", (Object) "nt:unstructured");
            userInputValues.put("firstName", firstName);
            userInputValues.put("lastName", lastName);
            userInputValues.put("age", age);
            userInputValues.put("country", country);


            ResourceUtil.getOrCreateResource(resourceResolver, application.getAnfFormPath() + Constants.SLASH + "data_"+new Date().getTime(), userInputValues,
                    JcrConstants.NT_UNSTRUCTURED, true);
            resourceResolver.commit();

        } catch (IOException e) {
            logger.error("IO Exception in commitUserDetails API Execution : {}", e);
        }
    }
}
