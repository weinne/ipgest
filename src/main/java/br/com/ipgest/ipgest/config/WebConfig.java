package br.com.ipgest.ipgest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig class that implements WebMvcConfigurer to configure web-related settings.
 * This configuration class registers the GlobalAttributesInterceptor to the InterceptorRegistry.
 * 
 * Annotations:
 * @Configuration - Indicates that this class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
 * 
 * Fields:
 * @Autowired
 * private GlobalAttributesInterceptor globalAttributesInterceptor - Interceptor to add global attributes to the model.
 * 
 * Methods:
 * @Override
 * public void addInterceptors(@NonNull InterceptorRegistry registry) - Adds the globalAttributesInterceptor to the provided InterceptorRegistry.
 * 
 * @param registry - InterceptorRegistry to which the interceptor is added.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalAttributesInterceptor globalAttributesInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(globalAttributesInterceptor);
    }
}