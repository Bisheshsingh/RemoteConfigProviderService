package com.remote.config.remoteconfigproviderservice;

import com.flagsmith.FlagsmithClient;
import com.flagsmith.exceptions.FlagsmithApiError;
import com.flagsmith.exceptions.FlagsmithClientError;
import com.flagsmith.models.Flags;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigServer
public class RemoteConfigProviderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemoteConfigProviderServiceApplication.class, args);
    }
}
