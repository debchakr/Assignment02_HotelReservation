package com.cgi.hotel.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.cgi.hotel.reservation.listener.BankTransferSubscriber;

@Configuration
public class RedisConfig {

    @Bean
    RedisMessageListenerContainer container(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container =
            new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(
            listenerAdapter,
            new PatternTopic("bank-transfer-payment-update"));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(
        BankTransferSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }
}