package com.bilalalp.entropyinitializer;


import com.bilalalp.common.constant.QueueConfigConstant;
import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.entropyinitializer.service.TeamVarianceService;
import com.bilalalp.entropyinitializer.service.TvCalcService;
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
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(SpringMongoConfig.class)
@PropertySource(value = {"classpath:amqp.properties"})
@ComponentScan(value = {"com.bilalalp.entropyinitializer"})
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class QueueConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private TeamVarianceService teamVarianceService;

    @Autowired
    private TvCalcService tvCalcService;

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
    public Binding selectorQueueBinding() {
        return BindingBuilder.bind(tvQueue())
                .to(amqpDirectExchange())
                .with(tvQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Binding tvCalcQueueBinding() {
        return BindingBuilder.bind(tvCalcQueue())
                .to(amqpDirectExchange())
                .with(tvCalcQueueConfiguration().getQueueKey())
                .noargs();
    }

    @Bean
    public Queue tvQueue() {
        return new Queue(tvQueueConfiguration().getQueueName());
    }

    @Bean
    public Queue tvCalcQueue() {
        return new Queue(tvCalcQueueConfiguration().getQueueName());
    }

    @Bean
    public MessageListenerContainer selectorQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(teamVarianceService);
        simpleMessageListenerContainer.setQueueNames(tvQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer tvCalcQueueContainer() {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMessageConverter(messageConverter());
        simpleMessageListenerContainer.setMessageListener(tvCalcService);
        simpleMessageListenerContainer.setQueueNames(tvCalcQueueConfiguration().getQueueName());
        return simpleMessageListenerContainer;
    }

    @Qualifier(value = "tvQueueConfiguration")
    @Bean
    public QueueConfigurationDto tvQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_TV_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_TV_QUEUE_NAME));
        return queueConfigurationDto;
    }

    @Qualifier(value = "tvCalcQueueConfiguration")
    @Bean
    public QueueConfigurationDto tvCalcQueueConfiguration() {
        final QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
        queueConfigurationDto.setExchangeName(environment.getProperty(QueueConfigConstant.AMQP_DIRECT_NAME));
        queueConfigurationDto.setQueueKey(environment.getProperty(QueueConfigConstant.AMQP_TV_CALC_QUEUE_KEY));
        queueConfigurationDto.setQueueName(environment.getProperty(QueueConfigConstant.AMQP_TV_CALC_QUEUE_NAME));
        return queueConfigurationDto;
    }
}