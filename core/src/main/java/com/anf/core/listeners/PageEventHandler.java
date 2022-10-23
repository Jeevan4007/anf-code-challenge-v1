package com.anf.core.listeners;

import java.util.Iterator;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.constants.Constants;
import com.anf.core.services.ResourceResolverService;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;

@Component(
    service = EventHandler.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.REQUIRE,
    property = { EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC })
@Designate(ocd = PageEventHandler.Config.class)
public class PageEventHandler implements EventHandler {

    private final Logger logger = LoggerFactory.getLogger(PageEventHandler.class);

    private Config config;

    @Reference
    ResourceResolverService resourceResolverService;

    public void handleEvent(final Event event) {
        try (ResourceResolver resourceResolver = resourceResolverService.getServiceResourceResolver()) {
            Iterator<PageModification> modifications = PageEvent.fromEvent(event).getModifications();
            while (modifications.hasNext()) {
                final PageModification pageModification = modifications.next();
                String pagePath = pageModification.getPath();
                if (pageModification.getType().equals(PageModification.ModificationType.CREATED)
                        && StringUtils.startsWith(pagePath, config.contentRoot())) {
                    Resource pageContentResource = resourceResolver
                            .getResource(pagePath + Constants.SLASH + JcrConstants.JCR_CONTENT);
                    if (Objects.nonNull(pageContentResource)) {
                        ModifiableValueMap pageValueMap = pageContentResource.adaptTo(ModifiableValueMap.class);
                        if (Objects.nonNull(pageValueMap)) {
                            pageValueMap.put("pageCreated", true);
                            resourceResolver.commit();
                        }

                    }
                }
            }

        } catch (LoginException e) {
            logger.error("Error Occurred in Page Event Handler on Page Creation : {}", e);
        } catch (Exception e) {
            logger.info("Error while adding property to page while creating page - {} ", e);
        }
    }

    @Activate
    @Modified
    public void activate(Config config) {
        this.config = config;
    }

    @ObjectClassDefinition(name = "ANF Page Event Handler")
    public @interface Config {

        String contentRoot() default "/content/anf-code-challenge/us/en";

    }
}
