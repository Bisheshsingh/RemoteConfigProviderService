package com.remote.config.remoteconfigproviderservice;

import com.flagsmith.FlagsmithClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigServer
public class RemoteConfigProviderServiceApplication {
    @Bean
    public FlagsmithClient getFlagsmithClient(@Value("${API_TOKEN}") final String apiKey) {
        return FlagsmithClient.newBuilder()
                .setApiKey(apiKey)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(RemoteConfigProviderServiceApplication.class, args);
    }
}
