/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.egli.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Helper configuration class for registering JAX-WS service beans properly in Spring.
 * @see <a href="http://stackoverflow.com/questions/25177491/spring-boot-register-jax-ws-webservice-as-bean">stackoverflow.com: register JAX-WS as bean/</a>
 *
 * @author Christian Egli
 * @since 01.11.2016
 */
@Configuration
public class WebApplicationContextLocator implements ServletContextInitializer {

    private static WebApplicationContext webApplicationContext;

    public static WebApplicationContext getCurrentWebApplicationContext() {
        return webApplicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }
}