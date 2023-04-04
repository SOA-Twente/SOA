package com.example.TimelineConsumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/**
 * Configuration sets the needed authentication for the connection factory.
 */
@Configuration
public class TimelineConsumerConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("localhost");
        connectionFactory.setUserName("username");
        connectionFactory.setPassword("password");
        return connectionFactory;
    }

}
