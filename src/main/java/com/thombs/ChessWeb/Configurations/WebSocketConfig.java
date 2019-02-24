package com.thombs.ChessWeb.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {
	
	@Autowired
    private Environment environment;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		//config.enableSimpleBroker("/topic", "/queue");
		config.setApplicationDestinationPrefixes("/chess");
		config.enableStompBrokerRelay("/topic", "/queue")
			.setRelayHost(environment.getRequiredProperty("rabbitmq.host"))
			.setRelayPort(environment.getRequiredProperty("rabbitmq.port", Integer.class))
			.setClientLogin(environment.getRequiredProperty("rabbitmq.username"))
			.setClientPasscode(environment.getRequiredProperty("rabbitmq.password"))
			.setSystemLogin(environment.getRequiredProperty("rabbitmq.username"))
			.setSystemPasscode(environment.getRequiredProperty("rabbitmq.password"));
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//Use this as the URL on the client side
		registry.addEndpoint("/chessEndpoint").withSockJS();
	}
}
