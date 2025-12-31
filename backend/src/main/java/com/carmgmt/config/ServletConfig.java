package com.carmgmt.config;

import com.carmgmt.servlet.FuelStatsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet configuration
 */
@Configuration
public class ServletConfig {
    
    /**
     * Register FuelStatsServlet
     */
    @Bean
    public ServletRegistrationBean<FuelStatsServlet> fuelStatsServletRegistration(
            FuelStatsServlet fuelStatsServlet) {
        ServletRegistrationBean<FuelStatsServlet> registration = 
                new ServletRegistrationBean<>(fuelStatsServlet, "/servlet/fuel-stats");
        registration.setName("fuelStatsServlet");
        return registration;
    }
}

