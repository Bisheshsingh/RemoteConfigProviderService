package com.remote.config.remoteconfigproviderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class RemoteConfigProviderServiceApplication {
    public static void main(String[] args) {
        System.out.println(System.getenv());

        SpringApplication.run(RemoteConfigProviderServiceApplication.class, args);
    }
}
