package com.bilalalp.dispatcher.config;

import com.bilalalp.dispatcher.amqp.DispatcherRequestErrorListener;
import com.bilalalp.dispatcher.amqp.DispatcherRequestListener;
import com.bilalalp.dispatcher.dto.QueueConfigurationDto;
import com.bilalalp.dispatcher.constant.ConfigConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@ComponentScan(basePackages = {"com.bilalalp"})
@PropertySource(value = {"classpath:amqp.properties"})
@Configuration
public class QueueConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private DispatcherRequestListener dispatcherRequestListener;

    @Autowired
    private DispatcherRequestErrorListener dispatcherRequestErrorListener;

    @Bean
    public Connection rabbitConnection() {
        return rabbitConnectionFactory().createConnection();
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(environment.getProperty(ConfigConstant.AMQP_HOST));
        cachingConnectionFactory.setPort(environment.getProperty(ConfigConstant.AMQP_PORT, Integer.class));
        cachingConnectionFactory.setUsername(environment.getProperty(ConfigConstant.AMQP_USERNAME));
        cachingConnectionFactory.setPassword(environment.getProperty(ConfigConstant.AMQP_PASSWORD));
        cachingConnectionFactory.setConnectionTimeout(environment.getProperty(ConfigConstant.AMQP_CONNECTION_TIMEOUT, Integer.class));
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setChannelTransacted(environment.getProperty(ConfigConstant.AMQP_CHANNEL_TRANSACTED, Boolean.class));
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConnectionFactory(rabbitConnectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public Exchange amqpDirectExchange() {
        return new DirectExchange(environment.getProperty(ConfigConstant.AMQP_DIRECT_NAME));
    }

    @Bean
    public Binding dispatcherRequestQueueBinding() {
        final Queue dispatcherRequestQueue = new Queue(dispatcherRequestQueueConfiguration().getQueueName());

        return BindingBuilder.bind(dispatcherRequestQueue)
                .to(amqpDirectExchange())
                .with(dispatcherRequestQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding dispatcherRequestErrorQueueBinding() {
        final Queue dispatcherRequestErrorQueue = new Queue(dispatcherRequestErrorQueueConfiguration().getQueueName());

        return BindingBuilder.bind(dispatcherRequestErrorQueue)
                .to(amqpDirectExchange())
                .with(dispatcherRequestErrorQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public MessageListenerContainer dispatcherRequestQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(dispatcherRequestListener);
        simpleMessageListenerContainer.setQueueNames(dispatcherRequestQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer dispatcherRequestErrorQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(dispatcherRequestErrorListener);
        simpleMessageListenerContainer.setQueueNames(dispatcherRequestErrorQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Qualifier(value = "dispatcherRequestQueueConfiguration")
    @Bean
    public QueueConfigurationDto dispatcherRequestQueueConfiguration() {

        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(ConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(ConfigConstant.AMQP_DISPATCHER_REQUEST_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(ConfigConstant.AMQP_DISPATCHER_REQUEST_QUEUE_NAME));
        return queueConfigurationDto;
    }

    @Qualifier(value = "dispatcherRequestErrorQueueConfiguration")
    @Bean
    public QueueConfigurationDto dispatcherRequestErrorQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(ConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(ConfigConstant.AMQP_DISPATCHER_REQUEST_ERROR_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(ConfigConstant.AMQP_DISPATCHER_REQUEST_ERROR_QUEUE_NAME));
        return queueConfigurationDto;
    }
}