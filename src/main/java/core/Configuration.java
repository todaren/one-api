package core;

import core.exceptions.ConfigNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {

    /**
     * Create HashMap which contains all properties from property files.
     */
    public static Map<String, String> configurationProperties = new HashMap<>();

    /**
     * Initial method that load all Config in configurationProperties HashMap.
     * Should be run before all tests.
     */
    public static void setUp() {
    loadConfigs("src/main/resources/environment.properties");
    }

    /**
     * This method read provided config File and add all properties in configurationProperties HashMap.
     * @param configFile File name with properties
     */
    private static void loadConfigs(String configFile) {
        Properties envProp = new Properties();

        try {
            envProp.load(new FileInputStream(configFile));
        } catch (IOException e) {
            throw new ConfigNotFoundException(String.format("Config \"%s\" not found", configFile));
        }
        envProp.forEach((key, value) -> {
            configurationProperties.put(key.toString().trim(), value.toString().trim());
        });
    }
}
