package com.bilalalp.dispatcher.config;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.dispatcher.amqp.DispatcherRequestConsumer;
import com.bilalalp.dispatcher.amqp.DispatcherRequestErrorConsumer;
import com.bilalalp.dispatcher.amqp.LinkSearcherConsumer;
import com.bilalalp.dispatcher.amqp.LinkSearcherErrorConsumer;
import com.bilalalp.dispatcher.constant.QueueConfigConstant;
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

    @Autowired
    private DispatcherRequestConsumer dispatcherRequestConsumer;

    @Autowired
    private DispatcherRequestErrorConsumer dispatcherRequestErrorConsumer;

    @Autowired
    private LinkSearcherConsumer linkSearcherConsumer;

    @Autowired
    private LinkSearcherErrorConsumer linkSearcherErrorConsumer;

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
    public Binding dispatcherRequestErrorQueueBinding() {
        return BindingBuilder.bind(dispatcherRequestErrorQueue())
                .to(amqpDirectExchange())
                .with(dispatcherRequestErrorQueueConfiguration().getQueueKey())
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
    public Binding linkSearcherErrorQueueBinding() {
        return BindingBuilder.bind(linkSearcherErrorQueue())
                .to(amqpDirectExchange())
                .with(linkSearcherErrorQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Queue dispatcherRequestErrorQueue() {
        return new Queue(dispatcherRequestErrorQueueConfiguration().getQueueName());
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
    public Queue linkSearcherErrorQueue() {
        return new Queue(linkSearcherErrorQueueConfiguration().getQueueName());
    }

    @Bean
    public MessageListenerContainer dispatcherRequestQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(dispatcherRequestConsumer);
        simpleMessageListenerContainer.setQueueNames(dispatcherRequestQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer dispatcherRequestErrorQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(dispatcherRequestErrorConsumer);
        simpleMessageListenerContainer.setQueueNames(dispatcherRequestErrorQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer linkSearcherErrorQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(linkSearcherErrorConsumer);
        simpleMessageListenerContainer.setQueueNames(linkSearcherErrorQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer linkSearcherQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(linkSearcherConsumer);
        simpleMessageListenerContainer.setQueueNames(linkSearcherQueueConfiguration().getQueueName());
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

    @Qualifier(value = "dispatcherRequestErrorQueueConfiguration")
    @Bean
    public QueueConfigurationDto dispatcherRequestErrorQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_DISPATCHER_REQUEST_ERROR_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_DISPATCHER_REQUEST_ERROR_QUEUE_NAME));
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

    @Qualifier(value = "linkSearcherErrorQueueConfiguration")
    @Bean
    public QueueConfigurationDto linkSearcherErrorQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_LINK_SEARCHER_ERROR_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_LINK_SEARCHER_ERROR_QUEUE_NAME));
        return queueConfigurationDto;
    }
}