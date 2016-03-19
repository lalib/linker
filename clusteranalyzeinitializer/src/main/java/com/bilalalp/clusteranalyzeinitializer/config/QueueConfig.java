package com.bilalalp.clusteranalyzeinitializer.config;

import com.bilalalp.clusteranalyzeinitializer.service.AnalyzeInitializerConsumer;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(value = {"com.bilalalp.clusteranalyzeinitializer"})
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = {"classpath:amqp.properties"})
@Configuration
public class QueueConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private AnalyzeInitializerConsumer analyzeInitializerConsumer;

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
    public Binding carQueueBinding() {
        return BindingBuilder.bind(carQueue())
                .to(amqpDirectExchange())
                .with(carQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding cprQueueBinding() {
        return BindingBuilder.bind(cprQueue())
                .to(amqpDirectExchange())
                .with(cprQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Queue carQueue() {
        return new Queue(carQueueConfiguration().getQueueName());
    }

    @Bean
    public Queue cprQueue() {
        return new Queue(cprQueueConfiguration().getQueueName());
    }

    @Bean
    public MessageListenerContainer cprQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(cprQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer crQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setQueueNames(carQueueConfiguration().getQueueName());
        simpleMessageListenerContainer.setMessageListener(analyzeInitializerConsumer);
        return simpleMessageListenerContainer;
    }

    @Qualifier("carQueueConfiguration")
    @Bean
    public QueueConfigurationDto carQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_CAR_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_CAR_QUEUE_NAME));
        return queueConfigurationDto;
    }

    @Qualifier("cprQueueConfiguration")
    @Bean
    public QueueConfigurationDto cprQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_CPR_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_CPR_QUEUE_NAME));
        return queueConfigurationDto;
    }
}