package com.anf.core.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.anf.core.services.ResourceResolverService;

@Model(
    adaptables = { SlingHttpServletRequest.class, Resource.class },
    adapters = { NewsFeedModel.class },
    resourceType = NewsFeedModel.RESOURCE_TYPE_CONTAINER,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NewsFeedModel {

    protected static final String RESOURCE_TYPE_CONTAINER = "anf-code-challenge/components/content/newsfeed";
    
    private Logger logger = LoggerFactory.getLogger(NewsFeedModel.class);
    
    @Inject
    protected ResourceResolverService resourceResolverService;

    @ValueMapValue
    @Default(values = "/var/commerce/products/anf-code-challenge/newsData")
    public String newsFeedPath;
    
    private List<NewsFeed> newsFeedList = null;

    @PostConstruct
    public void init() {
        
        try (ResourceResolver resourceResolver = resourceResolverService.getServiceResourceResolver()) {
            
            Iterator<Resource> newsFeedItems = Objects.requireNonNull(resourceResolver.getResource(newsFeedPath)).listChildren();
            newsFeedList = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String currentDate = simpleDateFormat.format(new Date());
                
            while(newsFeedItems.hasNext()){
                final Resource newsFeedItemResource = newsFeedItems.next();
                ValueMap valueMap = newsFeedItemResource.adaptTo(ValueMap.class);
                newsFeedList.add(getNewsFeed(valueMap, currentDate));
            }
            
        } catch (LoginException e) {
            logger.error("Exception in News Feed Model Execution : {}", e.getMessage());
        }

    }
    
    private NewsFeed getNewsFeed(ValueMap valueMap, String currentDate) {

        NewsFeed feedItem = new NewsFeed();

        feedItem.setAuthor( (String) valueMap.getOrDefault("author", StringUtils.EMPTY));
        feedItem.setContent( (String) valueMap.getOrDefault("content", StringUtils.EMPTY));
        feedItem.setDescription( (String) valueMap.getOrDefault("description", StringUtils.EMPTY));
        feedItem.setTitle( (String) valueMap.getOrDefault("title", StringUtils.EMPTY));
        feedItem.setUrl( (String) valueMap.getOrDefault("url", StringUtils.EMPTY));
        feedItem.setUrlImage( (String) valueMap.getOrDefault("urlImage", StringUtils.EMPTY));
        feedItem.setDate(currentDate);

        return feedItem;
    }
    
    public List<NewsFeed> getNewsFeedList() {
        return newsFeedList != null ? Collections.unmodifiableList(newsFeedList) : Collections.emptyList();
    }

}
