package com.btb.odj.config;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${odj.data.threads:0}")
    private int threads;

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor topicExecutor() {
        if (threads <= 0) {
            threads = Runtime.getRuntime().availableProcessors();
        }

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(300);
        executor.setCorePoolSize(threads);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("TOPIC-");
        return executor;
    }

    @Bean
    public JmsListenerContainerFactory<?> topicConnectionFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer,
            @Qualifier("topicExecutor") Executor topicExecutor) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        // concurrency > 1 makes that same message is handled by multiple threads at the same time
        // https://stackoverflow.com/questions/3088814/how-can-i-handle-multiple-messages-concurrently-from-a-jms-topic-not-queue-wit
        factory.setTaskExecutor(topicExecutor);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueConnectionFactory(
            ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /**
     * Dynamically resolve pubsub vs queue so that we can use the same jmsTemplate
     */
    @Bean
    public DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver() {
            @Override
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain)
                    throws JMSException {
                pubSubDomain = destinationName.toLowerCase().endsWith(".topic");
                return super.resolveDestinationName(session, destinationName, pubSubDomain);
            }
        };
    }
}
