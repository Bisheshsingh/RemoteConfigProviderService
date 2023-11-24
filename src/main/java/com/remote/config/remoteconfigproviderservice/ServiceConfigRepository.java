package com.remote.config.remoteconfigproviderservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.flagsmith.FlagsmithClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceConfigRepository implements EnvironmentRepository {
    private FlagsmithClient flagsmithClient;

    @Override
    public Environment findOne(final String application, final String profile, final String label) {
        final Map<String, String> properties = new HashMap<>();
        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            final String fetchRawValue = flagsmithClient.getEnvironmentFlags()
                    .getFeatureValue(application).toString();

            flattenNode("", objectMapper.readTree(fetchRawValue), properties);
        } catch (Exception e) {
            properties.clear();
        }

        final PropertySource propertySource = new PropertySource(label, properties);
        final Environment environment = new Environment(application,
                profile, label, "master");

        environment.add(propertySource);

        return environment;
    }

    private static void flattenNode(final String currentPath, final JsonNode node,
                                    final Map<String, String> properties) {
        if (node.isObject()) {
            final Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

            while (fields.hasNext()) {
                final Map.Entry<String, JsonNode> entry = fields.next();

                flattenNode(currentPath + entry.getKey() + ".", entry.getValue(), properties);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                final String path = currentPath.substring(0, currentPath.length() - 1);

                flattenNode(path + "[" + i + "].", node.get(i), properties);
            }
        } else {
            properties.put(currentPath.substring(0, currentPath.length() - 1), node.asText());
        }
    }
}
