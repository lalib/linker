package com.bilalalp.dispatcher.config;

import com.bilalalp.common.constant.QueueConfigConstant;
import com.bilalalp.common.dto.QueueConfigurationDto;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = {"classpath:amqp.properties"})
@Configuration
public class QueueConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Connection rabbitConnection() {
        return rabbitConnectionFactory().createConnection();
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(environment.getProperty(QueueConfigConstant.AMQP_HOST));
        cachingConnectionFactory.setPort(environment.getProperty(QueueConfigConstant.AMQP_PORT, Integer.class));
        cachingConnectionFactory.setUsername(environment.getProperty(QueueConfigConstant.AMQP_USERNAME));
        cachingConnectionFactory.setPassword(environment.getProperty(QueueConfigConstant.AMQP_PASSWORD));
        cachingConnectionFactory.setConnectionTimeout(environment.getProperty(QueueConfigConstant.AMQP_CONNECTION_TIMEOUT, Integer.class));
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setChannelTransacted(environment.getProperty(QueueConfigConstant.AMQP_CHANNEL_TRANSACTED, Boolean.class));
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConnectionFactory(rabbitConnectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public Exchange amqpDirectExchange() {
        return new DirectExchange(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
    }

    @Bean
    public Binding dispatcherRequestQueueBinding() {
        return BindingBuilder.bind(dispatcherRequestQueue())
                .to(amqpDirectExchange())
                .with(dispatcherRequestQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding linkSearcherQueueBinding() {
        return BindingBuilder.bind(linkSearcherQueue())
                .to(amqpDirectExchange())
                .with(linkSearcherQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding selectorQueueBinding() {
        return BindingBuilder.bind(selectorQueue())
                .to(amqpDirectExchange())
                .with(selectorQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding tfIdfQueueBinding() {
        return BindingBuilder.bind(tfIdfQueue())
                .to(amqpDirectExchange())
                .with(tfIdfQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Queue linkSearcherQueue() {
        return new Queue(linkSearcherQueueConfiguration().getQueueName());
    }

    @Bean
    public Queue dispatcherRequestQueue() {
        return new Queue(dispatcherRequestQueueConfiguration().getQueueName());
    }

    @Bean
    public Queue selectorQueue() {
        return new Queue(selectorQueueConfiguration().getQueueName());
    }

    @Bean
    public Queue tfIdfQueue() {
        return new Queue(tfIdfQueueConfiguration().getQueueName());
    }

    @Bean
    public MessageListenerContainer linkSearcherQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(linkSearcherQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer dispatcherRequestQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(dispatcherRequestQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer selectorQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(selectorQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer tfIdfQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(tfIdfQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Qualifier(value = "dispatcherRequestQueueConfiguration")
    @Bean
    public QueueConfigurationDto dispatcherRequestQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_DISPATCHER_REQUEST_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_DISPATCHER_REQUEST_QUEUE_NAME));
        return queueConfigurationDto;
    }

    @Qualifier(value = "linkSearcherQueueConfiguration")
    @Bean
    public QueueConfigurationDto linkSearcherQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_LINK_SEARCHER_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_LINK_SEARCHER_QUEUE_NAME));
        return queueConfigurationDto;
    }

    @Qualifier(value = "selectorQueueConfiguration")
    @Bean
    public QueueConfigurationDto selectorQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_SELECTOR_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_SELECTOR_QUEUE_NAME));
        return queueConfigurationDto;
    }

    @Qualifier(value = "tfIdfQueueConfiguration")
    @Bean
    public QueueConfigurationDto tfIdfQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_TF_IDF_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_TF_IDF_QUEUE_NAME));
        return queueConfigurationDto;
    }
}