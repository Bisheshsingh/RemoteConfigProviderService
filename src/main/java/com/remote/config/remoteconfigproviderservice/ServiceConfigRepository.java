package com.remote.config.remoteconfigproviderservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flagsmith.FlagsmithClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceConfigRepository implements EnvironmentRepository {
    private FlagsmithClient flagsmithClient;
    private ObjectMapper objectMapper;

    @Override
    public Environment findOne(final String application, final String profile, final String label) {
        Map<String, String> properties;

        try {
            properties = objectMapper
                    .readValue(flagsmithClient.getEnvironmentFlags()
                            .getFeatureValue(application).toString(),
                            new TypeReference<>() {});
        } catch (Exception e) {
            properties = new HashMap<>();
        }

        final PropertySource propertySource = new PropertySource(label, properties);
        final Environment environment = new Environment(application,
                profile, label, "master");

        environment.add(propertySource);

        return environment;
    }
}
