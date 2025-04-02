package com.assessment2.serverconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.assessment2.handler.MsgWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketSeverConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MsgWebSocketHandler(), "/msg").setAllowedOrigins("*");
    }
}
