package com.stevenzcaho.localcatfindingsystem.config;

import com.stevenzcaho.localcatfindingsystem.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
public class WebsocketConfig {
    @Autowired
    private ChatRoomService service;

    @Bean
    public HandlerMapping webSocketHandlerMapping(){
        Map<String, WebSocketHandler> map = Map.of("/chat", service);
        return new SimpleUrlHandlerMapping(map,0);
    }
}
