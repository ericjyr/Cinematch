package com.cm.cinematchapp.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for creating a customized RestTemplate bean with specified timeouts.
 * This class configures a RestTemplate bean with custom connection and read timeouts.
 * The timeouts are set using a SimpleClientHttpRequestFactory.
 *
 * @author Mateus Souza
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean with a custom ClientHttpRequestFactory.
     *
     * @return A RestTemplate instance configured with custom timeouts.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }
    /**
     * Creates a SimpleClientHttpRequestFactory with custom timeouts.
     *
     * @return A ClientHttpRequestFactory instance with custom timeouts.
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // Set connection and read timeouts (in milliseconds)
        factory.setConnectTimeout(5000); // Example connection timeout
        factory.setReadTimeout(10000);    // Example read timeout
        return factory;
    }


}
