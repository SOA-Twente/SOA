package com.example.TimelineProducer.producer;

import org.springframework.beans.factory.annotation.Value;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class TimelineProducerConfig {

    @Value("${active-mq.broker-url}")
    private String brokerUrl;

    @Value("${active-mq.user}")
    private String user;

    @Value("${active-mq.password}")
    private String password;

    /**
     * The connection factory is used to create a connection to the message broker (activeMQ in this case).
     * The factory can be used for both queue connection and topic connection.
     * It provides a way to manage the URL, username, and password.
     * @return connectionFactory instance with the given properties.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setUserName(user);
        activeMQConnectionFactory.setPassword(password);

        return connectionFactory();
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }
}
