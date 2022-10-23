package com.anf.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ContentService {
	void commitUserDetails(String firstName, String lastName, String country, String age, ResourceResolver resolver);
}
