package com.anf.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anf.core.services.ResourceResolverService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class NewsFeedModelTest {
    
    private final AemContext context = new AemContext(ResourceResolverType.RESOURCERESOLVER_MOCK);
    private static final String NEWS_RESOURCE_PATH = "/content/anf/news";
    private static final String COMMERCE_PATH = "/var/commerce/products/anf-code-challenge/newsData";
    
    @Mock
    private NewsFeedModel newsFeedModel;
    
    @Mock
    private ModelFactory modelFactory;
    
    @Mock
    ResourceResolver resourceResolver;
    
    @Mock
    ResourceResolverFactory factory;
    
    @Mock
    private ResourceResolverService resourceResolverService;

    @BeforeEach
    void setUp() throws Exception {
        context.addModelsForClasses(NewsFeedModel.class);
        context.load().json("/com/anf/core/models/news.json", NEWS_RESOURCE_PATH);
        context.load().json("/com/anf/core/models/feeds.json", COMMERCE_PATH);
        context.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING,
                Integer.MAX_VALUE);
        context.registerService(ResourceResolverService.class, resourceResolverService);
        when(resourceResolverService.getServiceResourceResolver()).thenReturn(resourceResolver);
        lenient().when(resourceResolver.getResource("/var/commerce/products/anf-code-challenge/newsData"))
                .thenReturn(context.currentResource("/var/commerce/products/anf-code-challenge/newsData"));
    }

    @Test
    void testNewsFeed() {
        context.currentResource(NEWS_RESOURCE_PATH);
        newsFeedModel = context.request().adaptTo(NewsFeedModel.class);
        assertEquals(10, newsFeedModel.getNewsFeedList().size());
        assertEquals("Caroline Fox", newsFeedModel.getNewsFeedList().get(0).author);
        assertEquals("https://www.bbc.co.uk/sport/mixed-martial-arts/61057214", newsFeedModel.getNewsFeedList().get(0).url);
    }

}
