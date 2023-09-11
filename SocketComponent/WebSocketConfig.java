package com.tongdatech.winterspring.zczx.webSocketConfig.SocketComponent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.tongdatech.winterspring.zczx.config.Profiles;


@Profile(Profiles.hasWebSocket)
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
}
