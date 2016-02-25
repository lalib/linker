package com.bilalalp.tfidfinitializer.config;

import com.bilalalp.common.constant.QueueConfigConstant;
import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.tfidfinitializer.service.TfIdfInitializerService;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(value = {"com.bilalalp.tfidfinitializer"})
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = {"classpath:amqp.properties"})
@Configuration
public class QueueConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private TfIdfInitializerService tfIdfInitializerService;

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
    public Binding tfIdfQueueBinding() {
        return BindingBuilder.bind(tfIdfQueue())
                .to(amqpDirectExchange())
                .with(tfIdfQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding tfIdfProcessQueueBinding() {
        return BindingBuilder.bind(tfIdfProcessQueue())
                .to(amqpDirectExchange())
                .with(tfIdfProcessQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Queue tfIdfQueue() {
        return new Queue(tfIdfQueueConfiguration().getQueueName());
    }

    @Bean
    public Queue tfIdfProcessQueue() {
        return new Queue(tfIdfProcessQueueConfiguration().getQueueName());
    }

    @Bean
    public MessageListenerContainer tfIdfQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(tfIdfQueueConfiguration().getQueueName());
        simpleMessageListenerContainer.setMessageListener(tfIdfInitializerService);
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer tfIdfProcessQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(tfIdfProcessQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
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

    @Qualifier(value = "tfIdfProcessQueueConfiguration")
    @Bean
    public QueueConfigurationDto tfIdfProcessQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_TF_IDF_PROCESS_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_TF_IDF_PROCESS_QUEUE_NAME));
        return queueConfigurationDto;
    }
}