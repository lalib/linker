package com.bilalalp.dispatcher.config;

import com.bilalalp.dispatcher.webservice.DispatcherWebService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Getter
@Setter
@Configuration
public class WebServiceConfig {

    @Autowired
    private DispatcherWebService dispatcherWebService;

    @Bean(name = "cxf")
    public Bus bus() {
        final SpringBus springBus = new SpringBus();
        springBus.getInInterceptors().add(new LoggingInInterceptor());
        springBus.getOutInterceptors().add(new LoggingOutInterceptor());
        return springBus;
    }

    @Bean
    public Server restService() {
        final JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setAddress("/linker");
        endpoint.setBus(bus());
        endpoint.setProviders(Arrays.asList(jsonProvider(), jaxbElementProvider()));
        endpoint.setServiceBean(dispatcherWebService);
        return endpoint.create();
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJaxbJsonProvider(new ObjectMapper(), JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
    }

    @Bean
    public JAXBElementProvider jaxbElementProvider() {
        final JAXBElementProvider jaxbElementProvider = new JAXBElementProvider();
        jaxbElementProvider.setSingleJaxbContext(true);
        return jaxbElementProvider;
    }
}