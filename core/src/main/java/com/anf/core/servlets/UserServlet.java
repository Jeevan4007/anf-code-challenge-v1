package com.anf.core.servlets;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.services.ApplicationConfigService;
import com.anf.core.services.ContentService;
import com.anf.core.services.ResourceResolverService;

@Component(service = { Servlet.class })
@SlingServletPaths(value = "/bin/saveUserDetails")
public class UserServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private transient Logger logger = LoggerFactory.getLogger(UserServlet.class);

    @Reference
    transient ApplicationConfigService application;

    @Reference
    transient ContentService contentService;

    @Reference
    transient ResourceResolverService resourceResolverService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {
        try (ResourceResolver resourceResolver = resourceResolverService.getServiceResourceResolver()) {

            int currentAge = Integer.parseInt(req.getParameter("age"));
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String country = req.getParameter("country");

            // Getting resource from configured path (/etc/age)
            Resource resource = resourceResolver.getResource(application.getAgeLimitPath());

            assert resource != null;
            ValueMap valueMap = resource.adaptTo(ValueMap.class);

            // Max and Min age
            assert valueMap != null;
            int maxAge = Integer.parseInt(Objects.requireNonNull(valueMap.get("maxAge", String.class)));
            int minAge = Integer.parseInt(Objects.requireNonNull(valueMap.get("minAge", String.class)));

            // Sending response after validation
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(country)
                    && currentAge >= minAge && currentAge <= maxAge) {
                contentService.commitUserDetails(firstName, lastName, country, Integer.toString(currentAge), resourceResolver);
                resp.getWriter().write("true");
            } else {
                resp.getWriter().write("false");
            }

        } catch (LoginException e) {
            logger.error("Exception in Form submit Execution : {}", e.getMessage());
        }

    }
}
