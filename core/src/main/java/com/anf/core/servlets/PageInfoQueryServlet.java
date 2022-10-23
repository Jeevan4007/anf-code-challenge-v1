package com.anf.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.listeners.PageEventHandler;
import com.anf.core.services.ApplicationConfigService;
import com.anf.core.services.ResourceResolverService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;

/**
 * 
 * PageInfoQueryServlet - This class used to fetch first 10 pages under /content/anf-code-challenge/us/en
 *
 * Output: http://localhost:4502/content/anf-code-challenge/us/en/jcr:content.anfPageQuery.json
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
    resourceTypes = "anf-code-challenge/components/page",
    methods = HttpConstants.METHOD_GET,
    selectors = "anfPageQuery")
@ServiceDescription("First 10 pages with Query Builder API")

public class PageInfoQueryServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private transient Logger logger = LoggerFactory.getLogger(PageEventHandler.class);

    @Reference
    transient QueryBuilder queryBuilder;

    @Reference
    transient ResourceResolverService resourceResolverService;
    
    @Reference
    transient ApplicationConfigService application;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        try (ResourceResolver resourceResolver = resourceResolverService.getServiceResourceResolver()) {

            final Session session = resourceResolver.adaptTo(Session.class);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("path", application.getQueryPageRoot());
            hashMap.put("type", "cq:Page");
            hashMap.put("property", "jcr:content/anfCodeChallenge");
            hashMap.put("property.operation", "exists");
            hashMap.put("orderby", "@jcr:created");
            hashMap.put("p.limit", "10");

            Query query = queryBuilder.createQuery(PredicateGroup.create(hashMap), session);
            SearchResult result = query.getResult();

            List<String> pageList = new LinkedList<>();
            for (Hit hit : result.getHits()) {
                pageList.add(hit.getResource().getPath());
            }
            resp.setContentType("application/json");
            Gson gson = new Gson();
            resp.getWriter().println(gson.toJson(pageList));

        } catch (LoginException | RepositoryException | IOException e) {
            logger.error("Exception in Query Builder API Execution : {}", e.getMessage());
        }

    }
}
