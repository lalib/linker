package com.bilalalp.dispatcher.config;

import com.bilalalp.common.config.CommonConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@EnableTransactionManagement
@ComponentScan(value = {"com.bilalalp.dispatcher"})
@Getter
@Setter
@Configuration
public class DispatcherConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        final AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(DispatcherConfig.class, WebServiceConfig.class, QueueConfig.class, CommonConfig.class);
        servletContext.addListener(new ContextLoaderListener(ctx));

        final ServletRegistration.Dynamic servlet = servletContext.addServlet("CXFServlet", new CXFServlet());
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");
    }
}