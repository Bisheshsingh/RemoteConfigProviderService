package com.remote.config.remoteconfigproviderservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flagsmith.FlagsmithClient;
import com.flagsmith.exceptions.FlagsmithClientError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceConfigRepository implements EnvironmentRepository {
    @Value("${API_TOKEN}")
    private String apiKey;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Environment findOne(final String application, final String profile, final String label) {
        Map<String, String> properties;

        try {
            properties = loadCustomProperties(application);
        } catch (Exception e) {
            properties = new HashMap<>();
        }

        final PropertySource propertySource = new PropertySource("custom", properties);

        final Environment environment = new Environment(application, profile,
                label, "master");

        environment.add(propertySource);

        return environment;
    }

    private Map<String, String> loadCustomProperties(final String application) throws FlagsmithClientError, JsonProcessingException {
        final FlagsmithClient flagsmithClient = FlagsmithClient.newBuilder()
                .setApiKey(apiKey)
                .build();

        return objectMapper
                .readValue(flagsmithClient.getEnvironmentFlags()
                        .getFeatureValue(application).toString(), new TypeReference<>() {
                });
    }
}
