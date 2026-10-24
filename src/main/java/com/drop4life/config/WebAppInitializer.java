
package com.drop4life.config;

import jakarta.servlet.Filter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;

public class WebAppInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { HibernateConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected void customizeRegistration(
            ServletRegistration.Dynamic registration) {

        registration.setMultipartConfig(
            new MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),
                5 * 1024 * 1024,
                10 * 1024 * 1024,
                0
            )
        );
    }
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
            new AuthenticationFilter()
        };
    }
}
