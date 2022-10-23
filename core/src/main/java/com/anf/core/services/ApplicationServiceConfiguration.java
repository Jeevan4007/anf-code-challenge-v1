package com.anf.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Application Service Configuration")
public @interface ApplicationServiceConfiguration {

    /**
     * Attribute for Query Root Path.
     *
     * @return queryPageRoot
     */
    @AttributeDefinition(name = "Query Root Path", description = "ANF Query Root Path")
    String queryPageRoot() default "/content/anf-code-challenge/us/en";; 

}
