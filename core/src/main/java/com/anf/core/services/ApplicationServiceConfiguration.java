package com.anf.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Application Service Configuration")
public @interface ApplicationServiceConfiguration {

    @AttributeDefinition(name = "Query Root Path", description = "ANF Query Root Path")
    String queryPageRoot() default "/content/anf-code-challenge/us/en";
    
    @AttributeDefinition(name = "Country DAM JSON path", description = "Country DAM JSON path")
    String countryDAMJson() default "/content/dam/anf-code-challenge/exercise-1/countries.json";
    
    @AttributeDefinition(name = "Age Validation Limits", description = "Enter the age validation limit Node Path")
    String ageLimitPath() default "/etc/age";
    
    @AttributeDefinition(name = "ANF Form Store Location", description = "Enter the ANF Form Store Location")
    String anfFormPath() default "/var/anf-code-challenge";

}
