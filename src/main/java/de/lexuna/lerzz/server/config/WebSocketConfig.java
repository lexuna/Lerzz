package de.lexuna.lerzz.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Websocket config class to implement a Websocket
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableMongoHttpSession
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Method to register STOMP endpoints.
     *
     * @param registry the StompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/secured/quiz").withSockJS();
        registry.addEndpoint("/lerzz").withSockJS();
        registry.addEndpoint("/lerzz/invite").withSockJS();
    }

    /**
     *  Method to configure the message broker
     *
     * @param registry the MessageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/secured/user");
    }


}
