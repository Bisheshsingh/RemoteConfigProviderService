package com.remote.config.remoteconfigproviderservice;

import com.flagsmith.FlagsmithClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@SpringBootApplication
@EnableConfigServer
@EnableWebSecurity
public class RemoteConfigProviderServiceApplication {
    @Bean
    public UserDetailsService userDetailsService(@Autowired final PasswordEncoder passwordEncoder,
                                                 @Value("${USER_ID}") final String userId,
                                                 @Value("${USER_PASS}") final String password) {
        final UserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername(userId)
                .password(passwordEncoder.encode(password)).build());

        return manager;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
